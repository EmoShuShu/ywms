package com.ywms.service;

import com.ywms.dao.Report;
import com.ywms.dao.ReportRepository;
import com.ywms.dao.User;
import com.ywms.dao.WorkOrderRepository;
import com.ywms.dto.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Service
public class ReportServiceImpl implements ReportService{

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Override
    public Report getReportById(int id) {
        return reportRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public ReportDTO generateReport() {
        ReportDTO report = new ReportDTO();
        LocalDateTime now = LocalDateTime.now();

        
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusWeeks(1);

        LocalDateTime startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay();
        LocalDateTime endOfMonth = now.with(TemporalAdjusters.firstDayOfNextMonth()).toLocalDate().atStartOfDay();

        LocalDateTime startOfYear = now.with(TemporalAdjusters.firstDayOfYear()).toLocalDate().atStartOfDay();
        LocalDateTime endOfYear = now.with(TemporalAdjusters.firstDayOfNextYear()).toLocalDate().atStartOfDay();

        

        
        report.setWeekSendReportNumber(workOrderRepository.countBySendTimeBetween(startOfWeek, endOfWeek));
        report.setWeekFinishedReportNumber(workOrderRepository.countFinishedByFinishTimeBetween(startOfWeek, endOfWeek));
        report.setWeekUnfinishedReportNumber(report.getWeekSendReportNumber() - report.getWeekFinishedReportNumber());

        
        report.setMonthSendReportNumber(workOrderRepository.countBySendTimeBetween(startOfMonth, endOfMonth));
        report.setMonthFinishedReportNumber(workOrderRepository.countFinishedByFinishTimeBetween(startOfMonth, endOfMonth));
        report.setMonthUnfinishedReportNumber(report.getMonthSendReportNumber() - report.getMonthFinishedReportNumber());

        
        report.setYearSendReportNumber(workOrderRepository.countBySendTimeBetween(startOfYear, endOfYear));
        report.setYearFinishedReportNumber(workOrderRepository.countFinishedByFinishTimeBetween(startOfYear, endOfYear));
        report.setYearUnfinishedReportNumber(report.getYearSendReportNumber() - report.getYearFinishedReportNumber());

        
        report.setTotalSendReportNumber(workOrderRepository.countTotal());
        report.setTotalFinishedReportNumber(workOrderRepository.countTotalFinished());
        report.setTotalUnfinishedReportNumber(report.getTotalSendReportNumber() - report.getTotalFinishedReportNumber());

        return report;
    }

}
