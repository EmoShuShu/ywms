package com.ywms.controller;

import com.ywms.dao.ResponseOrder;
import com.ywms.dto.ResponseOrderRequest;
import com.ywms.service.Response;
import com.ywms.service.ResponseOrderService;
import com.ywms.service.WorkOrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/workorders") // Controller 的基础路径仍然是 /api/workorders
public class ResponseOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    //【关键】注入 ResponseOrderService
    @Autowired
    private ResponseOrderService responseOrderService;


    @PostMapping("/{orderId}/complete")
    public ResponseEntity<Response<?>> completeWorkOrder(
            @PathVariable String orderId, // 从 URL 路径中获取 workOrderId
            @RequestBody ResponseOrderRequest request,
            HttpSession session) {

        // 1. 获取并校验用户登录状态 (逻辑不变)
        Object userDbIdObj = session.getAttribute("userDbId");
        if (userDbIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.newFail("用户未登录"));
        }
        int operatorIdentityId = (int) userDbIdObj;

        try {
            // 2.【关键】调用 ResponseOrderService 来处理业务逻辑
            //    注意，我们将从路径中获取的 orderId 作为参数传进去
            ResponseOrder createdResponseOrder = responseOrderService.createResponseOrder(orderId, request, operatorIdentityId);

            // 3. 成功，返回新创建的回单对象
            return ResponseEntity.ok(Response.newSuccess(createdResponseOrder));

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.newFail(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.newFail(e.getMessage()));
        } catch (RuntimeException e) {
            // 这里可以更细化，比如判断 e.getMessage() 来区分是工单没找到还是用户没找到
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.newFail(e.getMessage()));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<List<ResponseOrder>> checkMyResponseOrders(HttpSession session) {
        // 从 session 中获取 "userId"
        // 这个 ID 对应的是 WorkOrder 表中的 applicantId
        Object userIdObj = session.getAttribute("userId");

        // 如果 session 中没有用户信息，说明用户未登录
        if (userIdObj == null) {
            // 返回 401 未授权状态码
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }

        Integer userId = (Integer) userIdObj;

        // 调用 service 层获取数据
        List<ResponseOrder> responseOrders = responseOrderService.findAllByApplicantId(userId);

        // 返回 200 OK 状态码，并附带数据列表
        return ResponseEntity.ok(responseOrders);
    }

    @GetMapping("/operator/check")
    public ResponseEntity<List<ResponseOrder>> checkOperatorResponseOrders(HttpSession session) {
        // 从 session 中获取 "userDbId"
        // 这个 ID 对应的是 responseUserId
        Object userIdObj = session.getAttribute("userDbId");

        // 如果 session 中没有用户信息，说明用户未登录
        if (userIdObj == null) {
            // 返回 401 未授权状态码
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }

        int userId = (int) userIdObj;

        // 调用 service 层获取数据
        List<ResponseOrder> responseOrders = responseOrderService.findAllByResponseUserId(userId);

        // 返回 200 OK 状态码，并附带数据列表
        return ResponseEntity.ok(responseOrders);
    }

    @GetMapping("/approver/check")
    public ResponseEntity<List<ResponseOrder>> checkApproverResponseOrders(HttpSession session) {
        // 从 session 中获取 "userDbId"
        // 这个 ID 对应的是 responseUserId
        Object userIdObj = session.getAttribute("userDbId");

        // 如果 session 中没有用户信息，说明用户未登录
        if (userIdObj == null) {
            // 返回 401 未授权状态码
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }

        int userId = (int) userIdObj;

        // 调用 service 层获取数据
        List<ResponseOrder> responseOrders = responseOrderService.findAllByApproverId(userId);

        // 返回 200 OK 状态码，并附带数据列表
        return ResponseEntity.ok(responseOrders);
    }

}
