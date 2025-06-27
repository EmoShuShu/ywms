package com.ywms.controller;

import com.ywms.dao.ReportRepository;
import com.ywms.dto.ReportDTO;
import com.ywms.service.ReportService;
import com.ywms.service.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workorders")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/report")
    public ResponseEntity<Response<?>> getReport() {
        
        try {
            ReportDTO report = reportService.generateReport();
            return ResponseEntity.ok(Response.newSuccess(report));
        } catch (Exception e) {
            
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.newFail("生成报告时发生错误：" + e.getMessage()));
        }
    }
}
