package com.cy.netty.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * handler 入口类
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/9/9
 */
public class MyChannelHandler extends ChannelInitializer<SocketChannel> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<ChannelHandler> handlerList = new ArrayList<>();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        logger.debug("1======================");
        ch.pipeline().addLast(new MyDecoderHandler());//解码
        ch.pipeline().addLast(new MyEncoderHandler());//编码
        ch.pipeline().addLast(new IdleStateHandler(60, 40, 25, TimeUnit.SECONDS));
//        ch.pipeline().addLast(new ServerHandler());
        if(handlerList!=null && handlerList.size()>0) {
            ChannelHandler[] a = new ChannelHandler[handlerList.size()];
            ch.pipeline().addLast(handlerList.toArray(a));
        }
    }

    public List<ChannelHandler> getHandlerList() {
        return handlerList;
    }

    public void setHandlerList(List<ChannelHandler> handlerList) {
        this.handlerList = handlerList;
    }
}
