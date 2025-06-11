package com.ywms.service;

import com.ywms.dao.Report;
import com.ywms.dto.ReportDTO;

public interface ReportService {
    public Report getReportById(int id);

    /**
     * 生成统计报告。
     * @return 包含各项统计数据的 ReportDTO 对象。
     */
    ReportDTO generateReport();
}
