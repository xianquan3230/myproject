package com.lcoil.mq.controller;
import com.lcoil.mq.contant.MqTag;
import com.lcoil.mq.contant.MqTopic;
import com.lcoil.mq.utils.MQUtil;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * @Classname RocketController
 * @Description TODO
 * @Date 2022/5/15 7:47 PM
 * @Created by l-coil
 */
@RestController
@RequestMapping("/rocketmq")
public class RocketController {

    @Resource
    private MQUtil mqUtil;

    @GetMapping("/test")
    public String test(String content) {
        return content;
    }

    @GetMapping("/test1")
    public String test1(String content) {
        mqUtil.sendMessage(content, MqTopic.TEST_TOPIC ,MqTag.ROCKETMQ_TEST1);
//        mqUtil.sendDelayMessage("测试", MqTag.ROCKETMQ_TEST1, 1000L);
        return "success";
    }

    @GetMapping("/test2")
    public String test2(String content) {
        mqUtil.sendMessage(content, MqTag.ROCKETMQ_TEST2);
        mqUtil.sendDelayMessage("测试", MqTag.ROCKETMQ_TEST2,3000L);
        return "success";
    }

}