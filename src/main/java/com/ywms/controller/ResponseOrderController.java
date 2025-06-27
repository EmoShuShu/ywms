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
@RequestMapping("/api/workorders") 
public class ResponseOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    
    @Autowired
    private ResponseOrderService responseOrderService;


    @PostMapping("/{orderId}/complete")
    public ResponseEntity<Response<?>> completeWorkOrder(
            @PathVariable String orderId, 
            @RequestBody ResponseOrderRequest request,
            HttpSession session) {

        
        Object userDbIdObj = session.getAttribute("userDbId");
        if (userDbIdObj == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.newFail("用户未登录"));
        }
        int operatorIdentityId = (int) userDbIdObj;

        try {
            
            
            ResponseOrder createdResponseOrder = responseOrderService.createResponseOrder(orderId, request, operatorIdentityId);

            
            return ResponseEntity.ok(Response.newSuccess(createdResponseOrder));

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.newFail(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.newFail(e.getMessage()));
        } catch (RuntimeException e) {
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.newFail(e.getMessage()));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Response<?>> checkMyResponseOrders(HttpSession session) {
        
        
        Object userIdObj = session.getAttribute("userDbId");

        
        if (userIdObj == null) {
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((Response<?>) Collections.emptyList());
        }

        Integer userDbId = (Integer) userIdObj;

        
        List<ResponseOrder> responseOrders = responseOrderService.findAllByApplicantId(userDbId);

        
        return ResponseEntity.ok(Response.newSuccess(responseOrders));
    }

    @GetMapping("/operator/check")
    public ResponseEntity<Response<?>> checkOperatorResponseOrders(HttpSession session) {
        
        
        Object userIdObj = session.getAttribute("userDbId");

        
        if (userIdObj == null) {
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((Response<?>) Collections.emptyList());
        }

        int userId = (int) userIdObj;

        
        List<ResponseOrder> responseOrders = responseOrderService.findAllByResponseUserId(userId);

        
        return ResponseEntity.ok(Response.newSuccess(responseOrders));
    }

    @GetMapping("/approver/check")
    public ResponseEntity<Response<?>> checkApproverResponseOrders(HttpSession session) {
        
        
        Object userIdObj = session.getAttribute("userDbId");

        
        if (userIdObj == null) {
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body((Response<?>) Collections.emptyList());
        }

        int userId = (int) userIdObj;

        
        List<ResponseOrder> responseOrders = responseOrderService.findAllByApproverId(userId);

        
        return ResponseEntity.ok(Response.newSuccess(responseOrders));
    }

}
