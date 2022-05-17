package com.lcoil.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.lcoil.commons.command.DemoCommand;
import com.lcoil.commons.commandbus.CommandBus;
import com.lcoil.commons.utils.ResponseResult;
import com.lcoil.params.git.GitEventParams;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CommandBus commandBus;

    @PostMapping("/callback")
    @ResponseBody
    public ResponseResult callback(@RequestBody GitEventParams gitEventParams){
        return new ResponseResult(200, "成功", JSONObject.toJSONString(gitEventParams));
    }

    @PostMapping("/demoCommand")
    public String check(@RequestBody DemoCommand demoCommand){
        commandBus.send(demoCommand);
        return "ok";
    }

}
