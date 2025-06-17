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

}
