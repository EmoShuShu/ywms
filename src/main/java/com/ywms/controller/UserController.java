package com.ywms.controller;

import com.ywms.converter.UserConverter;
import com.ywms.dao.User;
import com.ywms.dto.LoginRequest;
import com.ywms.dto.UserDTO;
import com.ywms.service.Response;
import com.ywms.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Response<?>> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {

        User authenticatedUser = userService.validateUser(loginRequest.getUserId(), loginRequest.getPassword());

        if (authenticatedUser != null) {
            

            
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", authenticatedUser.getUserId());
            session.setAttribute("userDbId", authenticatedUser.getIdentityId());
            

            
            UserDTO userDto = UserConverter.convertUser(authenticatedUser);

            
            Response<UserDTO> successResponse = Response.newSuccess(userDto);

            
            return ResponseEntity.ok(successResponse);

        } else {
            
            Response<Void> failResponse = Response.newFail("账号或密码错误");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Response<String>> logout(HttpServletRequest request) {
        
        
        
        
        HttpSession session = request.getSession(false);

        
        if (session != null) {
            
            session.invalidate();
        }

        
        
        return ResponseEntity.ok(Response.newSuccess("退出成功"));
    }
}
