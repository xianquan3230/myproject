package com.lcoil.mq.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * @Classname RocketMQConfig
 * @Description rocketmq 配置
 * @Date 2022/5/15 7:46 PM
 * @Created by l-coil
 */
@Configuration
public class RocketMQConfig {

    @Value("${rocketmq.accessKey}")
    public String accessKey;
    public static String ACCESS_KEY;

    @Value("${rocketmq.secretKey}")
    public String secretKey;
    public static String SECRET_KEY;

    @Value("${rocketmq.namesrvAddr}")
    public String namesrvAddr;
    public static String NAMESRV_ADDR;

    @Value("${rocketmq.groupId}")
    public String groupId;
    public static String GROUP_ID;

    @Value("${rocketmq.topic}")
    public String topic;
    public static String TOPIC;

    /**
     * 配置RocketMq参数
     * @return Properties
     */
    public Properties getProperties() {
        Properties properties = new Properties();
        //您在控制台创建的GroupID
        properties.put(PropertyKeyConst.GROUP_ID, groupId);
        // 鉴权用AccessKeyId在阿里云服务器管理控制台创建
        properties.setProperty(PropertyKeyConst.AccessKey, accessKey);
        // 鉴权用AccessKeySecret在阿里云服务器管理控制台创建
        properties.setProperty(PropertyKeyConst.SecretKey, secretKey);
        //延时时间
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, "3000");
        // 顺序消息消费失败进行重试前的等待时间单位(毫秒)
        properties.put(PropertyKeyConst.SuspendTimeMillis, "100");
        // 消息消费失败时的最大重试次数
        properties.put(PropertyKeyConst.MaxReconsumeTimes, "20");
        // 设置TCP接入域名,进入控制台的实例管理页面,在页面上方选择实例后,在实例信息中的“获取接入点信息”区域查看
        properties.put(PropertyKeyConst.NAMESRV_ADDR, namesrvAddr);
        return properties;
    }

    /**
     * 初始化静态常量
     */
    @PostConstruct
    public void init(){
        ACCESS_KEY = this.accessKey;
        SECRET_KEY = this.secretKey;
        NAMESRV_ADDR = this.namesrvAddr;
        GROUP_ID = this.groupId;
        TOPIC = this.topic;
    }
}