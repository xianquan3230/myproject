package com.lcoil.commons.commandbus;


import com.lcoil.commons.command.Command;

/**
 * @Classname CommandBus
 * @Description TODO
 * @Date 2022/2/19 8:16 PM
 * @Created by l-coil
 */
public interface CommandBus {
    boolean send(Command command);
}
