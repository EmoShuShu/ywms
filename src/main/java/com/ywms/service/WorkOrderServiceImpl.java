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

/**
 * WorkOrderService 的实现类，负责处理所有与工单相关的核心业务逻辑。
 */
@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    /**
     * 依赖注入 WorkOrderRepository，用于工单的数据库操作。
     */
    @Autowired
    private WorkOrderRepository workOrderRepository;

    /**
     * 依赖注入 UserRepository，用于获取用户信息。
     */
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResponseOrderRepository responseOrderRepository;


    /**
     * 创建一个新的工单。
     *
     * @param request             包含前端提交数据的 DTO 对象。
     * @param applicantIdentityId 当前登录用户的数据库主键 ID (identityId)。
     * @return 持久化到数据库后的 WorkOrder 对象，包含了自动生成的 orderId。
     */
    @Override
    public WorkOrder createWorkOrder(WorkOrderCreateRequest request, int applicantIdentityId) {

        // 1. 根据传入的主键ID，查找用户信息。如果找不到，则抛出异常，阻止非法操作。
        User applicant = userRepository.findById(applicantIdentityId)
                .orElseThrow(() -> new RuntimeException("创建工单失败：找不到主键ID为 " + applicantIdentityId + " 的用户"));

        // 2. 创建一个新的 WorkOrder 实体对象，准备填充数据。
        WorkOrder newWorkOrder = new WorkOrder();

        // 【关键改动】在保存之前，手动生成并设置 orderId
        String generatedId = generateCustomOrderId(applicantIdentityId);
        newWorkOrder.setOrderId(generatedId);

        // 3. 从前端的 DTO 中获取数据并设置到实体中。
        newWorkOrder.setIssueDescription(request.getIssueDescription());
        newWorkOrder.setType(request.getType());
        newWorkOrder.setDeadline(request.getDeadline());

        // 4. 从查出来的 User 对象中获取申请人信息。
        //    这里的 applicantId 存储的是用户的主键 identityId。
        newWorkOrder.setApplicantId(applicant.getIdentityId());
        newWorkOrder.setApplicantName(applicant.getIdentityName());
        newWorkOrder.setApplicantIdentity(applicant.getDepartmentA());

        // 5. 根据业务规则设置初始状态和时间。
        newWorkOrder.setOrderStatus(applicant.getDepartmentA()); // 初始状态=申请者等级
        newWorkOrder.setSendTime(LocalDateTime.now());

        User allocatedUser = userRepository.findRandomApproverByIdentityNumberNative(1).orElse(null);
        newWorkOrder.setAllocatedId(allocatedUser.getIdentityId());

        // 6. 将填充好的实体对象保存到数据库。
        //    JPA 在保存后会自动将数据库生成的 orderId 回填到 newWorkOrder 对象中。
        return workOrderRepository.save(newWorkOrder);
    }

    /**
     * 自定义工单ID生成器。
     * 规则：将申请者的主键ID与一个精确到毫秒的时间戳拼接成一个字符串。
     * 这种方法能提供极高的唯一性，并且生成的ID具有一定的时间可追溯性。
     *
     * @param applicantId 申请人的数据库主键 ID (identityId)。
     * @return 生成的唯一的、基于字符串的工单ID。例如："100120250612103055123"。
     */
    private String generateCustomOrderId(int applicantId) {

        // 1. 定义一个适合用作ID的时间格式化器。
        //    - yyyy: 4位年份
        //    - MM:   2位月份
        //    - dd:   2位日期
        //    - HH:   24小时制的小时
        //    - mm:   分钟
        //    - ss:   秒
        //    - SSS:  3位毫秒
        //    这个格式是紧凑的，不包含任何分隔符。
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        // 2. 获取当前时间，并使用上面的格式化器将其转换为字符串。
        String timePart = LocalDateTime.now().format(formatter);

        // 3. 将申请人ID和时间字符串直接拼接起来。
        //    Java 会自动将 int 类型的 applicantId 转换为字符串进行拼接。
        return applicantId + timePart;
    }


    /**
     * 根据当前登录用户的身份，获取其可见的工单列表。
     *
     * @param currentIdentityId 当前登录用户的数据库主键 ID (identityId)。
     * @return 对该用户可见的工单列表。
     */
    @Override
    public List<WorkOrder> getVisibleWorkOrders(int currentIdentityId) {

        // 1. 根据主键ID获取完整的当前用户信息，因为我们需要它的角色和部门信息。
        User currentUser = userRepository.findById(currentIdentityId)
                .orElseThrow(() -> new RuntimeException("无法获取工单列表：当前用户信息不存在"));

        // 2. 使用 switch 语句，根据用户的身份 (identityNumber) 执行不同的查询策略。
        switch (currentUser.getIdentityNumber()) {
            case 1: // 角色是：申请者
                // 查找所有 applicantId 是自己主键ID的工单。
                return workOrderRepository.findByApplicantId(currentIdentityId);

            case 2: // 角色是：审批人员
                // 查找所有 orderStatus 与自己审批级别 (departmentB) 相同的工单。
                return workOrderRepository.findByAllocatedId(currentIdentityId);

            case 3: // 角色是：操作人员
                // 查找所有 recipientId 是自己主键ID的工单。
                return workOrderRepository.findByRecipientId(currentIdentityId);

            default: // 角色未定义或无权查看
                // 返回一个空的列表，表示没有任何可见的工单。
                // 这样做比返回 null 更安全，可以防止调用方出现空指针异常。
                return new ArrayList<>();
        }
    }

    @Override
    public List<WorkOrder> getDoneWorkOrders(int currentIdentityId) {
        // 1. 根据主键ID获取完整的当前用户信息，因为我们需要它的角色和部门信息。
        User currentUser = userRepository.findById(currentIdentityId)
                .orElseThrow(() -> new RuntimeException("无法获取工单列表：当前用户信息不存在"));

        // 2. 使用 switch 语句，根据用户的身份 (identityNumber) 执行不同的查询策略。
        switch (currentUser.getIdentityNumber()) {
            case 1: // 角色是：申请者
                // 查找所有 applicantId 是自己主键ID的工单。
                return workOrderRepository.findByApplicantId(currentIdentityId);

            case 2: // 角色是：审批人员
                // 查找所有 orderStatus 与自己审批级别 (departmentB) 相同的工单。（已完成）
                switch(currentUser.getDepartmentB()){
                    case 1:
                        return workOrderRepository.findByApproverIdA(currentIdentityId);
                    case 2:
                        return workOrderRepository.findByApproverIdB(currentIdentityId);
                    case 3:
                        return workOrderRepository.findByApproverIdC(currentIdentityId);
                }

                return workOrderRepository.findByAllocatedId(currentIdentityId);

            case 3: // 角色是：操作人员
                // 查找所有 recipientId 是自己主键ID的工单。
                return workOrderRepository.findByRecipientId(currentIdentityId);

            default: // 角色未定义或无权查看
                // 返回一个空的列表，表示没有任何可见的工单。
                // 这样做比返回 null 更安全，可以防止调用方出现空指针异常。
                return new ArrayList<>();
        }
    }

    @Override
    public void revokeWorkOrder(String orderId, int currentIdentityId) {

        // 1. 根据工单ID查找工单，如果不存在，则直接抛出异常。
        WorkOrder workOrderToRevoke = workOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("操作失败：找不到ID为 " + orderId + " 的工单。"));

        // 2.【核心权限校验】
        // 检查工单的申请人ID (workOrderToRevoke.getApplicantId())
        // 是否与当前操作用户的ID (currentIdentityId) 一致。
        if (workOrderToRevoke.getApplicantId() != currentIdentityId) {
            // 如果不一致，说明该用户无权操作此工单，抛出权限异常。
            throw new SecurityException("无权操作：你不是该工单的发起人。");
        }

        // (可选) 增加其他业务规则校验
        // 例如：只允许在工单处于“待审批”状态时撤回
        // if (workOrderToRevoke.getOrderStatus() >= 2) { // 假设状态2表示已进入市级审批
        //     throw new IllegalStateException("操作失败：工单已被上级审批，无法撤回。");
        // }

        // 3. 校验通过，执行删除操作。
        workOrderRepository.deleteById(orderId);
    }

    @Override
    public WorkOrder modifyWorkOrder(String orderId, WorkOrderUpdateRequest request, int currentIdentityId) {

        // 1. 查找工单，找不到则抛异常
        WorkOrder workOrderToModify = workOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("修改失败：找不到ID为 " + orderId + " 的工单。"));

        // 2. 核心权限校验：确认是申请者本人
        if (workOrderToModify.getApplicantId() != currentIdentityId) {
            throw new SecurityException("无权操作：你不是该工单的发起人。");
        }

        // 3. 业务规则校验：检查工单状态是否允许修改
        //    只允许在“待区级审批”(状态为1)时修改
        if (workOrderToModify.getOrderStatus() != 1) {
            throw new IllegalStateException("修改失败：该工单已进入审批流程，无法修改。");
        }

        // 4. 将 DTO 中的新数据更新到从数据库查出的实体对象上
        workOrderToModify.setIssueDescription(request.getIssueDescription());
        workOrderToModify.setType(request.getType());

        // 注意：只更新允许修改的字段，其他字段如 applicantId, sendTime 等保持不变。

        // 5. 保存更新后的实体。
        //    因为 workOrderToModify 是从数据库查出来的持久化对象，
        //    JPA/Hibernate 的 .save() 方法会智能地执行 UPDATE 语句。
        return workOrderRepository.save(workOrderToModify);
    }


    @Override
    public WorkOrder reviewWorkOrder(String orderId, ReviewRequest request, int currentIdentityId) {

        // 1. 获取当前审批人员的信息
        User approver = userRepository.findById(currentIdentityId)
                .orElseThrow(() -> new RuntimeException("操作失败：审批人员信息不存在。"));

        // 2. 验证角色是否为审批人员 (identityNumber = 2)
        if (approver.getIdentityNumber() != 2) {
            throw new SecurityException("无权操作：你不是审批人员。");
        }

        // 3. 查找需要审批的工单
        WorkOrder workOrderToReview = workOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("操作失败：找不到ID为 " + orderId + " 的工单。"));

        // 4. 核心权限校验：检查审批人员的级别(departmentB)是否与工单当前状态(orderStatus)匹配
        //    例如，区级审批人员(departmentB=1)只能审批状态为1(待区级审批)的工单
        if (approver.getDepartmentB() != workOrderToReview.getOrderStatus()) {
            throw new SecurityException("无权操作：此工单不属于你的审批范围。");
        }

        // 5. 根据审批决定 (approved) 更新工单状态
        if (request.isApproved()) {
            // --- 审批通过 ---
            int currentStatus = workOrderToReview.getOrderStatus();
            //设置approverId
            switch (approver.getDepartmentB()) {
                case 1: {// 等级是一级
                    workOrderToReview.setApproverIdA(approver.getIdentityId());
                    User allocatedUser = userRepository.findRandomApproverByIdentityNumberNative(2).orElse(null);
                    workOrderToReview.setAllocatedId(allocatedUser.getIdentityId());
                }
                case 2: { // 等级是二级
                    workOrderToReview.setApproverIdB(approver.getIdentityId());
                    User allocatedUser = userRepository.findRandomApproverByIdentityNumberNative(3).orElse(null);
                    workOrderToReview.setAllocatedId(allocatedUser.getIdentityId());
                }
                case 3: // 角色是：操作人员
                    workOrderToReview.setApproverIdC(approver.getIdentityId());

            }

            if (currentStatus < 3) { // 假设3是最高审批级别
                workOrderToReview.setOrderStatus(currentStatus + 1);

            } else { // currentStatus == 3, 这是最终的省级审批通过
                //【关键修复点在这里！】

                // a. 状态变为“审批通过，待处理”
                workOrderToReview.setOrderStatus(4);

                // b.【新增逻辑】为工单分配一个操作人员
                //    需要一个规则来决定分配给谁。
                //    先用一个最简单的规则：分配给系统中第一个找到的操作人员。

                // 从数据库中查找一个操作人员 (identityNumber = 3)
                // findFirst... 是一个假设的方法，我们需要在 UserRepository 中定义它
                User operatorToAssign = userRepository.findRandomByIdentityNumberNative(3)
                        .orElseThrow(() -> new IllegalStateException("系统中没有可用的操作人员来接收工单！"));

                // 将这个操作人员的 ID 和姓名设置到工单中
                workOrderToReview.setRecipientId(operatorToAssign.getIdentityId());
                workOrderToReview.setRecipientName(operatorToAssign.getIdentityName());
            }

        } else {
            // --- 审批不通过 (打回) ---
            // 这对应手册中的 Sendback 功能
            workOrderToReview.setOrderStatus(-1); // -1: 工单被打回
        }

        // 6. 保存更新后的工单
        return workOrderRepository.save(workOrderToReview);
    }

    @Override
    public WorkOrder deliverWorkOrder(String orderId, DeliveryRequest request, int currentIdentityId) {

        // 1. 获取当前操作人员的信息
        User operator = userRepository.findById(currentIdentityId)
                .orElseThrow(() -> new RuntimeException("操作失败：操作人员信息不存在。"));

        // 2. 验证角色是否为操作人员 (identityNumber = 3)
        if (operator.getIdentityNumber() != 3) {
            throw new SecurityException("无权操作：你不是操作人员。");
        }

        // 3. 查找需要派送的工单
        WorkOrder workOrderToDeliver = workOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("操作失败：找不到ID为 " + orderId + " 的工单。"));

        // 4. 业务规则校验：检查工单状态是否为“审批通过”(orderStatus = 4)
        if (workOrderToDeliver.getOrderStatus() != 4) {
            throw new IllegalStateException("派送失败：此工单当前状态不是“待处理”，无法派送。");
        }

        // 5. (可选但强烈推荐) 权限校验：检查工单是否已指派给当前操作员
        //    注意：这一步需要一个前置步骤，即在审批最终通过时，为工单分配一个 recipientId。
        //    我们假设这个逻辑已经存在。
        if (workOrderToDeliver.getRecipientId() != currentIdentityId) {
            throw new SecurityException("无权操作：此工单未指派给你。");
        }

        // 6. 验证目标部门ID的有效性 (防止前端传一个无效的数字)
        int targetDepartmentId = request.getDepartmentId();
        if (targetDepartmentId < 1 || targetDepartmentId > 3) {
            throw new IllegalArgumentException("派送失败：无效的目标部门ID。");
        }

        // 7. 更新工单的 department 字段
        workOrderToDeliver.setDepartment(targetDepartmentId);

        // (可选) 可以在这里更新工单状态，比如进入一个新的“处理中”的状态，
        // 但根据你的手册，似乎没有这一步，我们就先保持不变。

        // 8. 保存更新后的工单
        return workOrderRepository.save(workOrderToDeliver);
    }

}