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

    /**
     * 查看对当前用户已完成的工单列表
     * HTTP Method: GET
     * URL: /api/workorders
     */
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

    /**
     * 查看对当前用户未完成的工单列表
     * HTTP Method: GET
     * URL: /api/workorders
     */
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
            @PathVariable String orderId, // 从URL路径中获取 orderId
            HttpSession session) {

        // 1. 从 Session 中安全地获取用户ID
        Object userDbIdObj = session.getAttribute("userDbId");
        if (userDbIdObj == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Response.newFail("用户未登录，无法执行操作"));
        }
        int currentIdentityId = (int) userDbIdObj;

        try {
            // 2. 调用 Service 层执行撤回逻辑
            workOrderService.revokeWorkOrder(orderId, currentIdentityId);

            // 3. 操作成功，返回 200 OK 或 204 No Content
            // 返回 200 OK 并附带成功信息是比较友好的做法
            return ResponseEntity.ok(Response.newSuccess(null)); // 成功，但没有数据体返回

        } catch (SecurityException e) {
            // 捕获权限异常，返回 403 Forbidden
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN) // 403 表示服务器理解请求，但拒绝执行
                    .body(Response.newFail(e.getMessage()));
        } catch (RuntimeException e) {
            // 捕获其他运行时异常，比如找不到工单
            // 返回 404 Not Found 比较合适
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response.newFail(e.getMessage()));
        }
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Response<?>> modifyWorkOrder(
            @PathVariable String orderId,
            @RequestBody WorkOrderUpdateRequest request, // 接收请求体中的JSON数据
            HttpSession session) {

        // 1. 从 Session 中获取用户ID
        Object userDbIdObj = session.getAttribute("userDbId");
        if (userDbIdObj == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Response.newFail("用户未登录，无法执行操作"));
        }
        int currentIdentityId = (int) userDbIdObj;

        try {
            // 2. 调用 Service 层执行修改逻辑
            WorkOrder updatedWorkOrder = workOrderService.modifyWorkOrder(orderId, request, currentIdentityId);

            // 3. 操作成功，返回 200 OK 和更新后的工单数据
            return ResponseEntity.ok(Response.newSuccess(updatedWorkOrder));

        } catch (SecurityException e) {
            // 捕获权限异常
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.newFail(e.getMessage()));
        } catch (IllegalStateException e) {
            // 捕获业务规则异常 (如状态不对无法修改)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Response.newFail(e.getMessage())); // 409 Conflict 是一个不错的选择
        } catch (RuntimeException e) {
            // 捕获其他运行时异常 (如找不到工单)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.newFail(e.getMessage()));
        }
    }


    @PostMapping("/{orderId}/review")
    public ResponseEntity<Response<?>> reviewWorkOrder(
            @PathVariable String orderId,
            @RequestBody ReviewRequest request,
            HttpSession session) {

        // 1. 获取并校验用户登录状态
        Object userDbIdObj = session.getAttribute("userDbId");
        if (userDbIdObj == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Response.newFail("用户未登录，无法执行操作"));
        }
        int currentIdentityId = (int) userDbIdObj;

        try {
            // 2. 调用 Service 层执行审批逻辑
            WorkOrder reviewedWorkOrder = workOrderService.reviewWorkOrder(orderId, request, currentIdentityId);

            // 3. 操作成功，返回 200 OK 和更新后的工单数据
            return ResponseEntity.ok(Response.newSuccess(reviewedWorkOrder));

        } catch (SecurityException e) {
            // 捕获权限异常
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.newFail(e.getMessage()));
        } catch (RuntimeException e) {
            // 捕获其他运行时异常 (如找不到工单)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.newFail(e.getMessage()));
        }
    }

    @PostMapping("/{orderId}/deliver")
    public ResponseEntity<Response<?>> deliverWorkOrder(
            @PathVariable String orderId,
            @RequestBody DeliveryRequest request,
            HttpSession session) {

        // 1. 获取并校验用户登录状态
        Object userDbIdObj = session.getAttribute("userDbId");
        if (userDbIdObj == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Response.newFail("用户未登录，无法执行操作"));
        }
        int currentIdentityId = (int) userDbIdObj;

        try {
            // 2. 调用 Service 层执行派送逻辑
            WorkOrder deliveredWorkOrder = workOrderService.deliverWorkOrder(orderId, request, currentIdentityId);

            // 3. 操作成功，返回 200 OK 和更新后的工单数据
            return ResponseEntity.ok(Response.newSuccess(deliveredWorkOrder));

        } catch (SecurityException e) {
            // 捕获权限异常
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.newFail(e.getMessage()));
        } catch (IllegalStateException | IllegalArgumentException e) {
            // 捕获业务规则异常 (状态不对、部门ID无效等)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.newFail(e.getMessage()));
        } catch (RuntimeException e) {
            // 捕获其他运行时异常 (如找不到工单)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.newFail(e.getMessage()));
        }
    }


}
