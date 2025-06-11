package com.ywms.dao;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="report")
public class Report {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column
    private int id;

    @Column
    private int weekSendReportNumber;

    @Column
    private int weekFinishedInTimeReportNumber;

    @Column
    private int weekUnfinishedInTimeReportNumber;

    @Column
    private int monthSendReportNumber;

    @Column
    private int monthFinishedInTimeReportNumber;

    @Column
    private int monthUnfinishedInTimeReportNumber;

    @Column
    private int yearSendReportNumber;

    @Column
    private int yearFinishedInTimeReportNumber;

    @Column
    private int yearUnfinishedInTimeReportNumber;

    @Column
    private int totalSendReportNumber;

    @Column
    private int totalFinishedInTimeReportNumber;

    @Column
    private int totalUnfinishedInTimeReportNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeekSendReportNumber() {
        return weekSendReportNumber;
    }

    public void setWeekSendReportNumber(int weekSendReportNumber) {
        this.weekSendReportNumber = weekSendReportNumber;
    }

    public int getWeekFinishedInTimeReportNumber() {
        return weekFinishedInTimeReportNumber;
    }

    public void setWeekFinishedInTimeReportNumber(int weekFinishedInTimeReportNumber) {
        this.weekFinishedInTimeReportNumber = weekFinishedInTimeReportNumber;
    }

    public int getWeekUnfinishedInTimeReportNumber() {
        return weekUnfinishedInTimeReportNumber;
    }

    public void setWeekUnfinishedInTimeReportNumber(int weekUnfinishedInTimeReportNumber) {
        this.weekUnfinishedInTimeReportNumber = weekUnfinishedInTimeReportNumber;
    }

    public int getMonthSendReportNumber() {
        return monthSendReportNumber;
    }

    public void setMonthSendReportNumber(int monthSendReportNumber) {
        this.monthSendReportNumber = monthSendReportNumber;
    }

    public int getMonthFinishedInTimeReportNumber() {
        return monthFinishedInTimeReportNumber;
    }

    public void setMonthFinishedInTimeReportNumber(int monthFinishedInTimeReportNumber) {
        this.monthFinishedInTimeReportNumber = monthFinishedInTimeReportNumber;
    }

    public int getMonthUnfinishedInTimeReportNumber() {
        return monthUnfinishedInTimeReportNumber;
    }

    public void setMonthUnfinishedInTimeReportNumber(int monthUnfinishedInTimeReportNumber) {
        this.monthUnfinishedInTimeReportNumber = monthUnfinishedInTimeReportNumber;
    }

    public int getYearSendReportNumber() {
        return yearSendReportNumber;
    }

    public void setYearSendReportNumber(int yearSendReportNumber) {
        this.yearSendReportNumber = yearSendReportNumber;
    }

    public int getYearFinishedInTimeReportNumber() {
        return yearFinishedInTimeReportNumber;
    }

    public void setYearFinishedInTimeReportNumber(int yearFinishedInTimeReportNumber) {
        this.yearFinishedInTimeReportNumber = yearFinishedInTimeReportNumber;
    }

    public int getYearUnfinishedInTimeReportNumber() {
        return yearUnfinishedInTimeReportNumber;
    }

    public void setYearUnfinishedInTimeReportNumber(int yearUnfinishedInTimeReportNumber) {
        this.yearUnfinishedInTimeReportNumber = yearUnfinishedInTimeReportNumber;
    }

    public int getTotalSendReportNumber() {
        return totalSendReportNumber;
    }

    public void setTotalSendReportNumber(int totalSendReportNumber) {
        this.totalSendReportNumber = totalSendReportNumber;
    }

    public int getTotalFinishedInTimeReportNumber() {
        return totalFinishedInTimeReportNumber;
    }

    public void setTotalFinishedInTimeReportNumber(int totalFinishedInTimeReportNumber) {
        this.totalFinishedInTimeReportNumber = totalFinishedInTimeReportNumber;
    }

    public int getTotalUnfinishedInTimeReportNumber() {
        return totalUnfinishedInTimeReportNumber;
    }

    public void setTotalUnfinishedInTimeReportNumber(int totalUnfinishedInTimeReportNumber) {
        this.totalUnfinishedInTimeReportNumber = totalUnfinishedInTimeReportNumber;
    }
}
