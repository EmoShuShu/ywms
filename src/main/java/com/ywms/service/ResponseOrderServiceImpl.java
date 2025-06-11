package com.ywms.service;

import com.ywms.dao.*;
import com.ywms.dto.ResponseOrderRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ResponseOrderServiceImpl implements ResponseOrderService{

    @Autowired
    private ResponseOrderRepository responseOrderRepository;
    @Autowired
    private WorkOrderRepository workOrderRepository; // 依赖注入 WorkOrderRepository
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseOrder getResponseOrderById(String id) {
        return responseOrderRepository.findById(id).orElseThrow(RuntimeException::new);

    }

    @Override
    @Transactional // 【推荐】这是一个跨多个表的操作，应该在一个事务里完成
    public ResponseOrder createResponseOrder(String workOrderId, ResponseOrderRequest request, int operatorIdentityId) {

        // --- 1. 权限和状态校验 ---
        User operator = userRepository.findById(operatorIdentityId)
                .orElseThrow(() -> new RuntimeException("操作失败：找不到主键ID为 " + operatorIdentityId + " 的用户。"));
        if (operator.getIdentityNumber() != 3) {
            throw new SecurityException("无权操作：你不是操作人员。");
        }

        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new RuntimeException("操作失败：找不到ID为 " + workOrderId + " 的工单。"));
        if (workOrder.getRecipientId() != operatorIdentityId) {
            throw new SecurityException("无权操作：此工单未指派给你。");
        }

        if (workOrder.getOrderStatus() != 4 || workOrder.getDepartment() == 0) {
            throw new IllegalStateException("操作失败：此工单尚未派送，无法回单。");
        }
        if (workOrder.getOrderStatus() >= 5) {
            throw new IllegalStateException("操作失败：此工单已关闭，无法重复回单。");
        }

        // --- 2. 更新原始工单 (WorkOrder) ---
        workOrder.setFinishTime(LocalDateTime.now());
        if (request.getResponseStatus() == 1) { // 已完成
            workOrder.setOrderStatus(5);
        } else { // 无法完成
            workOrder.setOrderStatus(6);
        }
        workOrderRepository.save(workOrder);

        // --- 3. 创建并保存回单 (ResponseOrder) ---
        ResponseOrder responseOrder = new ResponseOrder();
        responseOrder.setResponseId(generateCustomResponseId(operatorIdentityId));
        responseOrder.setWorkOrderId(workOrderId);
        responseOrder.setResponseDescription(request.getResponseDescription());
        responseOrder.setResponseStatus(String.valueOf(request.getResponseStatus()));
        responseOrder.setResponseUserId(operator.getIdentityId());
        responseOrder.setOperatorName(operator.getIdentityName());

        return responseOrderRepository.save(responseOrder);
    }

    private String generateCustomResponseId(int operatorId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timePart = LocalDateTime.now().format(formatter);
        return "R" + operatorId + timePart;
    }

    @Override
    public ResponseOrder getResponseOrderByWorkOrderId(String workOrderId, int currentIdentityId) {

        // 1.【权限校验前置步骤】必须先获取工单信息，因为申请人ID在工单上。
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new RuntimeException("查看失败：找不到ID为 " + workOrderId + " 的工单。"));

        // 2.【核心权限校验】检查当前用户是否是该工单的申请人。
        if (workOrder.getApplicantId() != currentIdentityId) {
            throw new SecurityException("无权操作：你不是该工单的发起人，无法查看回单。");
        }

        // 3. 权限校验通过，现在可以根据工单ID去查找回单了。
        //    使用我们刚刚在 Repository 中定义的新方法。
        return responseOrderRepository.findByWorkOrderId(workOrderId)
                .orElseThrow(() -> new RuntimeException("查看失败：该工单还没有回单。"));
    }


}
