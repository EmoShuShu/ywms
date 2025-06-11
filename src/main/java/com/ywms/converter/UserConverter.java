package com.ywms.converter;

import com.ywms.dao.User;
import com.ywms.dto.UserDTO;

public class UserConverter {
    public static UserDTO convertUser (User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setIdentityId(user.getIdentityId());
        userDTO.setIdentityName(user.getIdentityName());
        userDTO.setIdentityNumber(user.getIdentityNumber());
        userDTO.setDepartmentA(user.getDepartmentA());
        userDTO.setDepartmentB(user.getDepartmentB());
        userDTO.setDepartmentC(user.getDepartmentC());

        return userDTO;

    }
}
