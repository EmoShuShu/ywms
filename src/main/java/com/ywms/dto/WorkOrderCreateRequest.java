package com.ywms.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkOrderCreateRequest {

    private String issueDescription;

    private int type;

    private LocalDateTime deadline;

}
