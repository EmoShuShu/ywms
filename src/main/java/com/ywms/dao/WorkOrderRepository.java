package com.ywms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkOrderRepository extends JpaRepository <WorkOrder,String>{

    

    
    List<WorkOrder> findByApplicantId(int applicantId);

    List<WorkOrder> findByApproverIdA(int allocatedId);

    List<WorkOrder> findByApproverIdB(int allocatedId);

    List<WorkOrder> findByApproverIdC(int allocatedId);

    
    List<WorkOrder> findByOrderStatus(int status);

    
    List<WorkOrder> findByRecipientId(int recipientId);

    List<WorkOrder> findByAllocatedId(int allocatedId);

    
    
    
    
    @Query("SELECT COUNT(w) FROM WorkOrder w WHERE w.sendTime >= :startTime AND w.sendTime < :endTime")
    int countBySendTimeBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    
    @Query("SELECT COUNT(w) FROM WorkOrder w WHERE w.finishTime >= :startTime AND w.finishTime < :endTime AND w.orderStatus IN (5, 6)")
    int countFinishedByFinishTimeBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    
    
    @Query("SELECT COUNT(w) FROM WorkOrder w")
    int countTotal();

    
    @Query("SELECT COUNT(w) FROM WorkOrder w WHERE w.orderStatus IN (5, 6)")
    int countTotalFinished();

    List<WorkOrder> findByApplicantId(Integer applicantId);

}
