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
        // 这个接口通常不需要登录，或者是管理员权限，我们暂时简化为公开访问
        try {
            ReportDTO report = reportService.generateReport();
            return ResponseEntity.ok(Response.newSuccess(report));
        } catch (Exception e) {
            // 捕获可能的数据库异常等
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.newFail("生成报告时发生错误：" + e.getMessage()));
        }
    }
}
