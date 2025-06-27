package com.ywms.service;

import com.ywms.dao.*;
import com.ywms.dto.DeliveryRequest;
import com.ywms.dto.ReviewRequest;
import com.ywms.dto.WorkOrderCreateRequest;
import com.ywms.dto.WorkOrderUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    
    @Autowired
    private WorkOrderRepository workOrderRepository;

    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResponseOrderRepository responseOrderRepository;


    
    @Override
    public WorkOrder createWorkOrder(WorkOrderCreateRequest request, int applicantIdentityId) {

        
        User applicant = userRepository.findById(applicantIdentityId)
                .orElseThrow(() -> new RuntimeException("创建工单失败：找不到主键ID为 " + applicantIdentityId + " 的用户"));

        
        WorkOrder newWorkOrder = new WorkOrder();

        
        String generatedId = generateCustomOrderId(applicantIdentityId);
        newWorkOrder.setOrderId(generatedId);

        
        newWorkOrder.setIssueDescription(request.getIssueDescription());
        newWorkOrder.setType(request.getType());
        newWorkOrder.setDeadline(request.getDeadline());

        
        
        newWorkOrder.setApplicantId(applicant.getIdentityId());
        newWorkOrder.setApplicantName(applicant.getIdentityName());
        newWorkOrder.setApplicantIdentity(applicant.getDepartmentA());

        
        newWorkOrder.setOrderStatus(applicant.getDepartmentA()); 
        newWorkOrder.setSendTime(LocalDateTime.now());

        User allocatedUser = userRepository.findRandomApproverByIdentityNumberNative(1).orElse(null);
        newWorkOrder.setAllocatedId(allocatedUser.getIdentityId());

        
        
        return workOrderRepository.save(newWorkOrder);
    }

    
    private String generateCustomOrderId(int applicantId) {

        
        
        
        
        
        
        
        
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        
        String timePart = LocalDateTime.now().format(formatter);

        
        
        return applicantId + timePart;
    }


    
    @Override
    public List<WorkOrder> getVisibleWorkOrders(int currentIdentityId) {

        
        User currentUser = userRepository.findById(currentIdentityId)
                .orElseThrow(() -> new RuntimeException("无法获取工单列表：当前用户信息不存在"));

        
        switch (currentUser.getIdentityNumber()) {
            case 1: 
                
                return workOrderRepository.findByApplicantId(currentIdentityId);

            case 2: 
                
                return workOrderRepository.findByAllocatedId(currentIdentityId);

            case 3: 
                
                return workOrderRepository.findByRecipientId(currentIdentityId);

            default: 
                
                
                return new ArrayList<>();
        }
    }

    @Override
    public List<WorkOrder> getDoneWorkOrders(int currentIdentityId) {
        
        User currentUser = userRepository.findById(currentIdentityId)
                .orElseThrow(() -> new RuntimeException("无法获取工单列表：当前用户信息不存在"));

        
        switch (currentUser.getIdentityNumber()) {
            case 1: 
                
                return workOrderRepository.findByApplicantId(currentIdentityId);

            case 2: 
                
                switch(currentUser.getDepartmentB()){
                    case 1:
                        return workOrderRepository.findByApproverIdA(currentIdentityId);
                    case 2:
                        return workOrderRepository.findByApproverIdB(currentIdentityId);
                    case 3:
                        return workOrderRepository.findByApproverIdC(currentIdentityId);
                }

                return workOrderRepository.findByAllocatedId(currentIdentityId);

            case 3: 
                
                return workOrderRepository.findByRecipientId(currentIdentityId);

            default: 
                
                
                return new ArrayList<>();
        }
    }

    @Override
    public void revokeWorkOrder(String orderId, int currentIdentityId) {

        
        WorkOrder workOrderToRevoke = workOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("操作失败：找不到ID为 " + orderId + " 的工单。"));

        
        
        
        if (workOrderToRevoke.getApplicantId() != currentIdentityId) {
            
            throw new SecurityException("无权操作：你不是该工单的发起人。");
        }

        
        
        
        
        

        
        workOrderRepository.deleteById(orderId);
    }

    @Override
    public WorkOrder modifyWorkOrder(String orderId, WorkOrderUpdateRequest request, int currentIdentityId) {

        
        WorkOrder workOrderToModify = workOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("修改失败：找不到ID为 " + orderId + " 的工单。"));

        
        if (workOrderToModify.getApplicantId() != currentIdentityId) {
            throw new SecurityException("无权操作：你不是该工单的发起人。");
        }

        
        
        if (workOrderToModify.getOrderStatus() != 1) {
            throw new IllegalStateException("修改失败：该工单已进入审批流程，无法修改。");
        }

        
        workOrderToModify.setIssueDescription(request.getIssueDescription());
        workOrderToModify.setType(request.getType());

        

        
        
        
        return workOrderRepository.save(workOrderToModify);
    }


    @Override
    public WorkOrder reviewWorkOrder(String orderId, ReviewRequest request, int currentIdentityId) {

        
        User approver = userRepository.findById(currentIdentityId)
                .orElseThrow(() -> new RuntimeException("操作失败：审批人员信息不存在。"));

        
        if (approver.getIdentityNumber() != 2) {
            throw new SecurityException("无权操作：你不是审批人员。");
        }

        
        WorkOrder workOrderToReview = workOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("操作失败：找不到ID为 " + orderId + " 的工单。"));

        
        
        if (approver.getDepartmentB() != workOrderToReview.getOrderStatus()) {
            throw new SecurityException("无权操作：此工单不属于你的审批范围。");
        }

        
        if (request.isApproved()) {
            
            int currentStatus = workOrderToReview.getOrderStatus();
            
            switch (approver.getDepartmentB()) {
                case 1: {
                    workOrderToReview.setApproverIdA(approver.getIdentityId());
                    User allocatedUser = userRepository.findRandomApproverByIdentityNumberNative(2).orElse(null);
                    workOrderToReview.setAllocatedId(allocatedUser.getIdentityId());
                }
                case 2: { 
                    workOrderToReview.setApproverIdB(approver.getIdentityId());
                    User allocatedUser = userRepository.findRandomApproverByIdentityNumberNative(3).orElse(null);
                    workOrderToReview.setAllocatedId(allocatedUser.getIdentityId());
                }
                case 3: 
                    workOrderToReview.setApproverIdC(approver.getIdentityId());

            }

            if (currentStatus < 3) { 
                workOrderToReview.setOrderStatus(currentStatus + 1);

            } else { 
                

                
                workOrderToReview.setOrderStatus(4);

                
                
                

                
                
                User operatorToAssign = userRepository.findRandomByIdentityNumberNative(3)
                        .orElseThrow(() -> new IllegalStateException("系统中没有可用的操作人员来接收工单！"));

                
                workOrderToReview.setRecipientId(operatorToAssign.getIdentityId());
                workOrderToReview.setRecipientName(operatorToAssign.getIdentityName());
            }

        } else {
            
            
            workOrderToReview.setOrderStatus(-1); 
        }

        
        return workOrderRepository.save(workOrderToReview);
    }

    @Override
    public WorkOrder deliverWorkOrder(String orderId, DeliveryRequest request, int currentIdentityId) {

        
        User operator = userRepository.findById(currentIdentityId)
                .orElseThrow(() -> new RuntimeException("操作失败：操作人员信息不存在。"));

        
        if (operator.getIdentityNumber() != 3) {
            throw new SecurityException("无权操作：你不是操作人员。");
        }

        
        WorkOrder workOrderToDeliver = workOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("操作失败：找不到ID为 " + orderId + " 的工单。"));

        
        if (workOrderToDeliver.getOrderStatus() != 4) {
            throw new IllegalStateException("派送失败：此工单当前状态不是“待处理”，无法派送。");
        }

        
        
        
        if (workOrderToDeliver.getRecipientId() != currentIdentityId) {
            throw new SecurityException("无权操作：此工单未指派给你。");
        }

        
        int targetDepartmentId = request.getDepartmentId();
        if (targetDepartmentId < 1 || targetDepartmentId > 3) {
            throw new IllegalArgumentException("派送失败：无效的目标部门ID。");
        }

        
        workOrderToDeliver.setDepartment(targetDepartmentId);

        
        

        
        return workOrderRepository.save(workOrderToDeliver);
    }

}