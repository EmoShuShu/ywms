package com.ywms.dao;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="workorder")
public class WorkOrder {

    @Id
    @Column
    private String orderId;

    @Column
    private String issueDescription;

    @Column
    private int orderStatus;

    @Column
    private String applicantName;

    @Column
    private int applicantId;

    @Column
    private int applicantIdentity;

    @Column
    private int recipientId;

    @Column
    private String recipientName;

    @Column
    private int type;

    @Column
    private int department;

    @Column
    private LocalDateTime sendTime;

    @Column
    private LocalDateTime finishTime;

    @Column
    private LocalDateTime deadline;

    @Column
    private int approverIdA;

    @Column
    private int approverIdB;

    @Column
    private int approverIdC;

    @Column
    private int allocatedId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }

    public int getApplicantIdentity() {
        return applicantIdentity;
    }

    public void setApplicantIdentity(int applicantIdentity) {
        this.applicantIdentity = applicantIdentity;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public int getApproverIdA() {
        return approverIdA;
    }

    public void setApproverIdA(int approveIdA) {
        this.approverIdA = approveIdA;
    }

    public int getApproverIdB() {
        return approverIdB;
    }

    public void setApproverIdB(int approverIdB) {
        this.approverIdB = approverIdB;
    }

    public int getApproverIdC() {
        return approverIdC;
    }

    public void setApproverIdC(int approverIdC) {
        this.approverIdC = approverIdC;
    }

    public int getAllocatedId() {
        return allocatedId;
    }

    public void setAllocatedId(int allocatedId) {
        this.allocatedId = allocatedId;
    }
}
