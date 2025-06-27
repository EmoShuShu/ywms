package com.ywms.service;

import com.ywms.dao.Report;
import com.ywms.dto.ReportDTO;

public interface ReportService {
    public Report getReportById(int id);

    
    ReportDTO generateReport();
}
