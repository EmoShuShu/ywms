package com.ywms.dto;

import lombok.Data;

@Data
public class WorkOrderUpdateRequest {

    // 新的工单问题描述
    private String issueDescription;

    // 新的工单类型 (1:故障, 2:维护, 3:后勤)
    private int type;

}