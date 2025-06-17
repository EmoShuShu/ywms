package com.ywms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkOrderRepository extends JpaRepository <WorkOrder,String>{

    // 新增查询方法

    /**
     * 根据申请人ID查找所有工单 (用于申请者查看)
     * @param applicantId 申请人的用户ID
     * @return 该申请人的工单列表
     */
    List<WorkOrder> findByApplicantId(int applicantId);

    List<WorkOrder> findByApproverIdA(int allocatedId);

    List<WorkOrder> findByApproverIdB(int allocatedId);

    List<WorkOrder> findByApproverIdC(int allocatedId);

    /**
     * 根据工单状态查找所有工单 (用于审批人员查看)
     * @param status 工单状态码
     * @return 对应状态的工单列表
     */
    List<WorkOrder> findByOrderStatus(int status);

    /**
     * 根据接收人ID查找所有工单 (用于操作人员查看)
     * @param recipientId 接收人的用户ID
     * @return 指派给该操作人员的工单列表
     */
    List<WorkOrder> findByRecipientId(int recipientId);

    List<WorkOrder> findByAllocatedId(int allocatedId);

    /**
     * 统计在指定时间范围内发起的工单总数。
     * 返回值为 int。
     */
    //【关键改动】使用 CAST(... AS int) 或者直接在方法签名中返回 int，
    // Spring Data JPA 通常能智能处理 Long 到 int 的转换，但显式转换更安全。
    // 我们先尝试让 Spring 自动转，如果不行再加 CAST。
    @Query("SELECT COUNT(w) FROM WorkOrder w WHERE w.sendTime >= :startTime AND w.sendTime < :endTime")
    int countBySendTimeBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 统计在指定时间范围内完成的工单总数。
     * 返回值为 int。
     */
    @Query("SELECT COUNT(w) FROM WorkOrder w WHERE w.finishTime >= :startTime AND w.finishTime < :endTime AND w.orderStatus IN (5, 6)")
    int countFinishedByFinishTimeBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    // JpaRepository 自带的 count() 返回的是 long，我们需要一个自定义的版本
    /**
     * 统计总共发起的工单数。
     * 返回值为 int。
     */
    @Query("SELECT COUNT(w) FROM WorkOrder w")
    int countTotal();

    /**
     * 统计总共完成的工单数。
     * 返回值为 int。
     */
    @Query("SELECT COUNT(w) FROM WorkOrder w WHERE w.orderStatus IN (5, 6)")
    int countTotalFinished();

    List<WorkOrder> findByApplicantId(Integer applicantId);

}
