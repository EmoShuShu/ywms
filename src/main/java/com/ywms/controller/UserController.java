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
            // --- 登录成功 ---

            // 1. 在 Session 中存储用户状态
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", authenticatedUser.getUserId());
            session.setAttribute("userDbId", authenticatedUser.getIdentityId());
            // ...

            // 2.【关键步骤】使用你的 UserConverter 进行转换
            UserDTO userDto = UserConverter.convertUser(authenticatedUser);

            // 3. 使用你的 Response.newSuccess() 封装 UserDTO
            Response<UserDTO> successResponse = Response.newSuccess(userDto);

            // 4. 将 Response 对象放入 ResponseEntity.ok() 中返回
            return ResponseEntity.ok(successResponse);

        } else {
            // --- 登录失败 ---
            Response<Void> failResponse = Response.newFail("账号或密码错误");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Response<String>> logout(HttpServletRequest request) {
        // 1. 获取当前的 Session。
        //    使用 request.getSession(false) 是一个安全的操作：
        //    - 如果用户已登录，它会返回当前的 session。
        //    - 如果用户未登录（或 session 已过期），它会返回 null，而不会创建一个新的 session。
        HttpSession session = request.getSession(false);

        // 2. 检查 session 是否存在
        if (session != null) {
            // 如果 session 存在，则将其销毁
            session.invalidate();
        }

        // 3. 无论用户之前是否登录，最终结果都是“未登录”状态。
        //    所以总是可以返回一个成功的响应。
        return ResponseEntity.ok(Response.newSuccess("退出成功"));
    }
}
