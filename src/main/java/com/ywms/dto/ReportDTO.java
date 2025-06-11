package com.ywms.dto;

import lombok.Data;

@Data
public class ReportDTO {
    // 本周统计
    private int weekSendReportNumber;
    private int weekFinishedReportNumber;
    private int weekUnfinishedReportNumber;

    // 本月统计
    private int monthSendReportNumber;
    private int monthFinishedReportNumber;
    private int monthUnfinishedReportNumber;

    // 本年统计
    private int yearSendReportNumber;
    private int yearFinishedReportNumber;
    private int yearUnfinishedReportNumber;

    // 总计
    private int totalSendReportNumber;
    private int totalFinishedReportNumber;
    private int totalUnfinishedReportNumber;
}