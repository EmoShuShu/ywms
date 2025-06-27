package com.ywms.service;

import com.ywms.dao.ResponseOrder;
import com.ywms.dto.ResponseOrderRequest;

import java.util.List;

public interface ResponseOrderService {
    public ResponseOrder getResponseOrderById(String id);

    
    ResponseOrder createResponseOrder(String workOrderId, ResponseOrderRequest request, int operatorIdentityId);

    
    ResponseOrder getResponseOrderByWorkOrderId(String workOrderId, int currentIdentityId);


    List<ResponseOrder> findAllByApplicantId(Integer applicantId);

    List<ResponseOrder> findAllByResponseUserId(int responseUserId);

    List<ResponseOrder> findAllByApproverId(int approverId);

}
