package com.ywms.dto;

import lombok.Data;

@Data
public class ResponseOrderRequest {

    // 回单描述，由操作人员填写
    private String responseDescription;

    // 回单状态 (1:已完成, 2:无法完成)
    private int responseStatus;
}