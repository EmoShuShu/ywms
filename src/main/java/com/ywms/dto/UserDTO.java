package com.ywms.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

public class UserDTO {


    private int userId;

    private int identityId;

    private String identityName;

    private int identityNumber;

    private int departmentA;

    private int departmentB;

    private int departmentC;



    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIdentityId() {
        return identityId;
    }

    public void setIdentityId(int identityId) {
        this.identityId = identityId;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public int getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(int identityNumber) {
        this.identityNumber = identityNumber;
    }

    public int getDepartmentA() {
        return departmentA;
    }

    public void setDepartmentA(int departmentA) {
        this.departmentA = departmentA;
    }

    public int getDepartmentB() {
        return departmentB;
    }

    public void setDepartmentB(int departmentB) {
        this.departmentB = departmentB;
    }

    public int getDepartmentC() {
        return departmentC;
    }

    public void setDepartmentC(int departmentC) {
        this.departmentC = departmentC;
    }



}
