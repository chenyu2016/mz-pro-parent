package com.cy.netty.util;

import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 请求数据包装
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/8/15
 */
public class Request {
    /**
     * 命令号
     */
    private int cmd;

    /**
     * 数据格式
     */
    private byte coderFlat;


    /**
     * 消息
     */
    private Object message;

    public Request(int cmd, byte coderFlat,Object message) {
        this.cmd = cmd;
        this.coderFlat = coderFlat;
        this.message = message;
    }

    public int getCmd() {
        return cmd;
    }

    public byte getCoderFlat() {
        return coderFlat;
    }

    public Object getMessage() {
        return message;
    }
}
