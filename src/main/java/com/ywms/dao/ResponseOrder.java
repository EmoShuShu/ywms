package com.ywms.dao;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="responseorder")
public class ResponseOrder {
    @Id
    @Column
    private String responseId;

    @Column
    private String responseDescription;

    @Column
    private String responseStatus;

    @Column
    private int responseUserId;

    @Column
    private String operatorName;

    @Column
    private int responseDepartment;

    @Column
    private String workOrderId;

    @Column
    private int approverIdA;

    @Column
    private int approverIdB;

    @Column
    private int approverIdC;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public int getResponseUserId() {
        return responseUserId;
    }

    public void setResponseUserId(int responseUserId) {
        this.responseUserId = responseUserId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public int getResponseDepartment() {
        return responseDepartment;
    }

    public void setResponseDepartment(int responseDepartment) {
        this.responseDepartment = responseDepartment;
    }

    public int getApproverIdA() {
        return approverIdA;
    }

    public void setApproverIdA(int approverIdA) {
        this.approverIdA = approverIdA;
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
}
