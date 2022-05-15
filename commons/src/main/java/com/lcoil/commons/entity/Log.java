package com.lcoil.commons.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @apiNote 日志
 * @author lcoil
 * @since 2020-05-24
 */
@Data
public class Log {

    private Long id;

    private String content;

    private String description;

    private String ip;

    private String module;

    private String username;

    private Date createAt;

    private Date updateAt;

    private Integer able;
    
}