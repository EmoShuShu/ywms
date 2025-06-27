package com.ywms.dto;

import lombok.Data;

@Data
public class ReportDTO {
    
    private int weekSendReportNumber;
    private int weekFinishedReportNumber;
    private int weekUnfinishedReportNumber;

    
    private int monthSendReportNumber;
    private int monthFinishedReportNumber;
    private int monthUnfinishedReportNumber;

    
    private int yearSendReportNumber;
    private int yearFinishedReportNumber;
    private int yearUnfinishedReportNumber;

    
    private int totalSendReportNumber;
    private int totalFinishedReportNumber;
    private int totalUnfinishedReportNumber;
}