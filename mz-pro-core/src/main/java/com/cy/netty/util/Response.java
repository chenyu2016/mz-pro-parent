package com.cy.netty.util;

import io.netty.channel.Channel;

/**
 * 响应数据包装
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/8/15
 */
public class Response {

    /**
     * 命令号
     */
    private int cmd;

    /**
     * 消息
     */
    private Object message;

    /**
     * 通道
     */
    private Channel channel;

    public Response(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
