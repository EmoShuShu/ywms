package com.ywms.service;

import com.ywms.dao.WorkOrder;
import com.ywms.dto.DeliveryRequest;
import com.ywms.dto.ReviewRequest;
import com.ywms.dto.WorkOrderCreateRequest;
import com.ywms.dto.WorkOrderUpdateRequest;

import java.util.List;

public interface WorkOrderService {

    WorkOrder createWorkOrder(WorkOrderCreateRequest request, int applicantIdentityId);

    List<WorkOrder> getVisibleWorkOrders(int currentUserId);

    List<WorkOrder> getDoneWorkOrders(int currentUserId);

    
    void revokeWorkOrder(String orderId, int currentIdentityId);

    
    WorkOrder modifyWorkOrder(String orderId, WorkOrderUpdateRequest request, int currentIdentityId);


    
    WorkOrder reviewWorkOrder(String orderId, ReviewRequest request, int currentIdentityId);

    
    WorkOrder deliverWorkOrder(String orderId, DeliveryRequest request, int currentIdentityId);
}
