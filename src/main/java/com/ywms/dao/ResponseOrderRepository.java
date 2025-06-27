package com.ywms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResponseOrderRepository extends JpaRepository<ResponseOrder,String> {

    
    Optional<ResponseOrder> findByWorkOrderId(String workOrderId);

    List<ResponseOrder> findByWorkOrderIdIn(List<String> workOrderIds);

    List<ResponseOrder> findByResponseUserId(int responseUserId);

    List<ResponseOrder> findByApproverIdAOrApproverIdBOrApproverIdC(int approverId1, int approverId2, int approverId3);
}
