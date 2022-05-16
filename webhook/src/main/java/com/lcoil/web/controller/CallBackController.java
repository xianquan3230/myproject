package com.lcoil.web.controller;

import com.lcoil.commons.utils.ResponseResult;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname CallBackController
 * @Description TODO
 * @Date 2022/5/16 5:11 PM
 * @Created by l-coil
 */
@RequestMapping("/wh")
@RestController
@AllArgsConstructor
public class CallBackController {
    @GetMapping("/callback")
    public ResponseResult callback(String request){
        return new ResponseResult(200, "成功", request);
    }

}
