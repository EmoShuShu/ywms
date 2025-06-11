package com.ywms.service;

import com.ywms.dao.ResponseOrder;
import com.ywms.dto.ResponseOrderRequest;

public interface ResponseOrderService {
    public ResponseOrder getResponseOrderById(String id);

    /**
     * 创建一个回单，并更新关联的工单状态。
     * @param workOrderId 关联的工单ID。
     * @param request 包含回单信息的DTO。
     * @param operatorIdentityId 当前操作人员的ID。
     * @return 创建成功的回单对象。
     */
    ResponseOrder createResponseOrder(String workOrderId, ResponseOrderRequest request, int operatorIdentityId);

    /**
     * 申请者根据工单ID查看回单。
     * @param workOrderId 要查看回单的工单ID。
     * @param currentIdentityId 当前申请者的ID。
     * @return 查找到的回单对象。
     */
    ResponseOrder getResponseOrderByWorkOrderId(String workOrderId, int currentIdentityId);

}
