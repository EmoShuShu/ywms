package com.ywms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResponseOrderRepository extends JpaRepository<ResponseOrder,String> {

    /**
     * 根据关联的工单ID查找回单。
     * 因为一个工单理论上只有一个回单，所以返回 Optional<ResponseOrder>。
     * @param workOrderId 关联的工单ID。
     * @return 包含回单信息的 Optional 对象。
     */
    Optional<ResponseOrder> findByWorkOrderId(String workOrderId);

    List<ResponseOrder> findByWorkOrderIdIn(List<String> workOrderIds);

    List<ResponseOrder> findByResponseUserId(int responseUserId);

    List<ResponseOrder> findByApproverIdAOrApproverIdBOrApproverIdC(int approverId1, int approverId2, int approverId3);
}
