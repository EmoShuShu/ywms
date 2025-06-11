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

    /**
     * 撤回一个工单。
     * @param orderId 要撤回的工单ID。
     * @param currentIdentityId 当前操作用户的数据库主键ID (identityId)。
     * @return void (操作成功无返回值，失败则抛出异常)。
     */
    void revokeWorkOrder(String orderId, int currentIdentityId);

    /**
     * 修改一个工单。
     * @param orderId 要修改的工单ID。
     * @param request 包含更新数据的DTO。
     * @param currentIdentityId 当前操作用户的数据库主键ID (identityId)。
     * @return 更新后的 WorkOrder 对象。
     */
    WorkOrder modifyWorkOrder(String orderId, WorkOrderUpdateRequest request, int currentIdentityId);


    /**
     * 审批一个工单。
     * @param orderId 要审批的工单ID。
     * @param request 包含审批决定的DTO。
     * @param currentIdentityId 当前审批人员的数据库主键ID (identityId)。
     * @return 更新状态后的 WorkOrder 对象。
     */
    WorkOrder reviewWorkOrder(String orderId, ReviewRequest request, int currentIdentityId);

    /**
     * 派送一个工单到具体执行部门。
     * @param orderId 要派送的工单ID。
     * @param request 包含目标部门ID的DTO。
     * @param currentIdentityId 当前操作人员的数据库主键ID (identityId)。
     * @return 更新部门后的 WorkOrder 对象。
     */
    WorkOrder deliverWorkOrder(String orderId, DeliveryRequest request, int currentIdentityId);
}
