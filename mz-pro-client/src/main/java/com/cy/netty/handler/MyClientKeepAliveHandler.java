package com.cy.netty.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/9/19
 */
public class MyClientKeepAliveHandler extends AbstractKeepAliveHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }
}
