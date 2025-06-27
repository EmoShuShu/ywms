package com.ywms.controller;

import com.ywms.dao.WorkOrder;
import com.ywms.dto.DeliveryRequest;
import com.ywms.dto.ReviewRequest;
import com.ywms.dto.WorkOrderCreateRequest;
import com.ywms.dto.WorkOrderUpdateRequest;
import com.ywms.service.Response;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.ywms.service.WorkOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workorders")
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @PostMapping
    public ResponseEntity<Response<?>> createWorkOrder(@RequestBody WorkOrderCreateRequest request, HttpSession session) {

        Object userDbIdObj = session.getAttribute("userDbId");

        if (userDbIdObj == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Response.newFail("用户未登录或会话已过期"));
        }

        int currentIdentityId = (int) userDbIdObj;

        try {
            WorkOrder createdWorkOrder = workOrderService.createWorkOrder(request, currentIdentityId);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Response.newSuccess(createdWorkOrder));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.newFail("创建工单失败：" + e.getMessage()));
        }
    }

    
    @GetMapping
    public ResponseEntity<Response<?>> getDoneWorkOrders(HttpSession session) {

        Object userDbIdObj = session.getAttribute("userDbId");

        if (userDbIdObj == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Response.newFail("用户未登录，无法查看工单"));
        }

        int currentIdentityId = (int) userDbIdObj;

        try {
            List<WorkOrder> workOrders = workOrderService.getDoneWorkOrders(currentIdentityId);

            return ResponseEntity.ok(Response.newSuccess(workOrders));

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.newFail("获取工单列表时发生内部错误：" + e.getMessage()));
        }
    }

    
    @GetMapping("/waiting")
    public ResponseEntity<Response<?>> getWorkOrders(HttpSession session) {

        Object userDbIdObj = session.getAttribute("userDbId");

        if (userDbIdObj == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Response.newFail("用户未登录，无法查看工单"));
        }

        int currentIdentityId = (int) userDbIdObj;

        try {
            List<WorkOrder> workOrders = workOrderService.getVisibleWorkOrders(currentIdentityId);

            return ResponseEntity.ok(Response.newSuccess(workOrders));

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.newFail("获取工单列表时发生内部错误：" + e.getMessage()));
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Response<Void>> revokeWorkOrder(
            @PathVariable String orderId, 
            HttpSession session) {

        
        Object userDbIdObj = session.getAttribute("userDbId");
        if (userDbIdObj == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Response.newFail("用户未登录，无法执行操作"));
        }
        int currentIdentityId = (int) userDbIdObj;

        try {
            
            workOrderService.revokeWorkOrder(orderId, currentIdentityId);

            
            
            return ResponseEntity.ok(Response.newSuccess(null)); 

        } catch (SecurityException e) {
            
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN) 
                    .body(Response.newFail(e.getMessage()));
        } catch (RuntimeException e) {
            
            
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response.newFail(e.getMessage()));
        }
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Response<?>> modifyWorkOrder(
            @PathVariable String orderId,
            @RequestBody WorkOrderUpdateRequest request, 
            HttpSession session) {

        
        Object userDbIdObj = session.getAttribute("userDbId");
        if (userDbIdObj == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Response.newFail("用户未登录，无法执行操作"));
        }
        int currentIdentityId = (int) userDbIdObj;

        try {
            
            WorkOrder updatedWorkOrder = workOrderService.modifyWorkOrder(orderId, request, currentIdentityId);

            
            return ResponseEntity.ok(Response.newSuccess(updatedWorkOrder));

        } catch (SecurityException e) {
            
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.newFail(e.getMessage()));
        } catch (IllegalStateException e) {
            
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Response.newFail(e.getMessage())); 
        } catch (RuntimeException e) {
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.newFail(e.getMessage()));
        }
    }


    @PostMapping("/{orderId}/review")
    public ResponseEntity<Response<?>> reviewWorkOrder(
            @PathVariable String orderId,
            @RequestBody ReviewRequest request,
            HttpSession session) {

        
        Object userDbIdObj = session.getAttribute("userDbId");
        if (userDbIdObj == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Response.newFail("用户未登录，无法执行操作"));
        }
        int currentIdentityId = (int) userDbIdObj;

        try {
            
            WorkOrder reviewedWorkOrder = workOrderService.reviewWorkOrder(orderId, request, currentIdentityId);

            
            return ResponseEntity.ok(Response.newSuccess(reviewedWorkOrder));

        } catch (SecurityException e) {
            
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.newFail(e.getMessage()));
        } catch (RuntimeException e) {
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.newFail(e.getMessage()));
        }
    }

    @PostMapping("/{orderId}/deliver")
    public ResponseEntity<Response<?>> deliverWorkOrder(
            @PathVariable String orderId,
            @RequestBody DeliveryRequest request,
            HttpSession session) {

        
        Object userDbIdObj = session.getAttribute("userDbId");
        if (userDbIdObj == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Response.newFail("用户未登录，无法执行操作"));
        }
        int currentIdentityId = (int) userDbIdObj;

        try {
            
            WorkOrder deliveredWorkOrder = workOrderService.deliverWorkOrder(orderId, request, currentIdentityId);

            
            return ResponseEntity.ok(Response.newSuccess(deliveredWorkOrder));

        } catch (SecurityException e) {
            
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.newFail(e.getMessage()));
        } catch (IllegalStateException | IllegalArgumentException e) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.newFail(e.getMessage()));
        } catch (RuntimeException e) {
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.newFail(e.getMessage()));
        }
    }


}
