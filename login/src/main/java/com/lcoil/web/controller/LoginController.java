package com.lcoil.web.controller;

import com.lcoil.commons.annotation.Log;
import com.lcoil.commons.utils.ResponseResult;
import com.lcoil.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: l-coil
 * @Date: 2022/2/24
 * @Time: 10:34
 * @Version: 1.0
 * @Description:
 */
@RequestMapping("/user")
@RestController
@AllArgsConstructor
public class LoginController {

    @PostMapping("/login")
    public ResponseResult login(){
        User user = new User();
        return new ResponseResult(200, "成功", user);
    }

    @GetMapping("/find")
    @Log(module = "查找用户",description = "查找用户")
    public ResponseResult find() {
        return new ResponseResult(200, "找到一个人", new User());
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user) {
        return new ResponseResult(200, "成功", "结果");
    }

    @GetMapping("/test")
    public ResponseResult getOne(){
        return new ResponseResult(200, "成功", "结果");
    }
}

