package com.ywms.dto;

import lombok.Data;

@Data
public class DeliveryRequest {

    /**
     * 目标部门ID
     * 1: 故障维修部门
     * 2: 维护部门
     * 3: 后勤保障部门
     */
    private int departmentId;
}