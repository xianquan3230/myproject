package com.lcoil.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.lcoil.commons.utils.ResponseResult;
import com.lcoil.params.git.GitEventParams;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/callback")
    @ResponseBody
    public ResponseResult callback(@RequestBody GitEventParams gitEventParams){
        return new ResponseResult(200, "成功", JSONObject.toJSONString(gitEventParams));
    }

}
