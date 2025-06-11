package com.ywms.dto;

import lombok.Data;

@Data
public class ReviewRequest {

    /**
     * 审批决定
     * true: 审批通过
     * false: 审批不通过 (打回)
     */
    private boolean approved;

}