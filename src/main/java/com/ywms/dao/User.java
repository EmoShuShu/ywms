package com.ywms.dao;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="user")
public class User {


    @Column(unique = true, nullable = false)
    private int userId;

    @Column
    private String password;

    @Id
    @Column
    @GeneratedValue(strategy = IDENTITY)
    private int identityId;

    @Column
    private String identityName;

    @Column
    private int identityNumber;

    @Column
    private int departmentA;

    @Column
    private int departmentB;

    @Column
    private int departmentC;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getDepartmentC() {
        return departmentC;
    }

    public void setDepartmentC(int departmentC) {
        this.departmentC = departmentC;
    }

    public int getDepartmentB() {
        return departmentB;
    }

    public void setDepartmentB(int departmentB) {
        this.departmentB = departmentB;
    }

    public int getDepartmentA() {
        return departmentA;
    }

    public void setDepartmentA(int departmentA) {
        this.departmentA = departmentA;
    }

    public int getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(int identityNumber) {
        this.identityNumber = identityNumber;
    }
}
