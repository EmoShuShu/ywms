package com.ywms.service;

import com.ywms.dao.*;
import com.ywms.dto.ResponseOrderRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResponseOrderServiceImpl implements ResponseOrderService{

    @Autowired
    private ResponseOrderRepository responseOrderRepository;
    @Autowired
    private WorkOrderRepository workOrderRepository; 
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseOrder getResponseOrderById(String id) {
        return responseOrderRepository.findById(id).orElseThrow(RuntimeException::new);

    }

    @Override
    @Transactional 
    public ResponseOrder createResponseOrder(String workOrderId, ResponseOrderRequest request, int operatorIdentityId) {

        
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

        
        workOrder.setFinishTime(LocalDateTime.now());
        if (request.getResponseStatus() == 1) { 
            workOrder.setOrderStatus(5);
        } else { 
            workOrder.setOrderStatus(6);
        }
        workOrderRepository.save(workOrder);

        
        ResponseOrder responseOrder = new ResponseOrder();
        responseOrder.setResponseId(generateCustomResponseId(operatorIdentityId));
        responseOrder.setWorkOrderId(workOrderId);
        responseOrder.setResponseDescription(request.getResponseDescription());
        responseOrder.setResponseStatus(String.valueOf(request.getResponseStatus()));
        responseOrder.setResponseUserId(operator.getIdentityId());
        responseOrder.setOperatorName(operator.getIdentityName());
        responseOrder.setApproverIdA(workOrder.getApproverIdA());
        responseOrder.setApproverIdB(workOrder.getApproverIdB());
        responseOrder.setApproverIdC(workOrder.getApproverIdC());

        return responseOrderRepository.save(responseOrder);
    }

    private String generateCustomResponseId(int operatorId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timePart = LocalDateTime.now().format(formatter);
        return "R" + operatorId + timePart;
    }

    @Override
    public ResponseOrder getResponseOrderByWorkOrderId(String workOrderId, int currentIdentityId) {

        
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new RuntimeException("查看失败：找不到ID为 " + workOrderId + " 的工单。"));

        
        if (workOrder.getApplicantId() != currentIdentityId) {
            throw new SecurityException("无权操作：你不是该工单的发起人，无法查看回单。");
        }

        
        
        return responseOrderRepository.findByWorkOrderId(workOrderId)
                .orElseThrow(() -> new RuntimeException("查看失败：该工单还没有回单。"));
    }

    @Override
    public List<ResponseOrder> findAllByApplicantId(Integer applicantId) {
        
        List<WorkOrder> workOrders = workOrderRepository.findByApplicantId(applicantId);

        if (workOrders.isEmpty()) {
            return Collections.emptyList();
        }

        
        
        List<String> workOrderIds = workOrders.stream()
                .map(WorkOrder::getOrderId) 
                .collect(Collectors.toList());

        
        return responseOrderRepository.findByWorkOrderIdIn(workOrderIds);
        
    }

    @Override
    public List<ResponseOrder> findAllByResponseUserId(int responseUserId) {

        return responseOrderRepository.findByResponseUserId(responseUserId);
        
    }

    @Override
    public List<ResponseOrder> findAllByApproverId(int approverId) {
        return responseOrderRepository.findByApproverIdAOrApproverIdBOrApproverIdC(approverId,approverId,approverId);
    }


}
