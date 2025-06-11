package com.ywms.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private Integer userId;

    private String password;
}