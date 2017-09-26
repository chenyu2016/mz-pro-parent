package com.cy.netty.handler;

import com.cy.netty.medium.Media;
import com.cy.netty.util.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/9/9
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        Request request = (Request)msg;
        Object object = Media.getInstance().process(request,ctx.channel());
        if(object==null){
            logger.error("处理结果为空");
            return;
        }
        ctx.channel().writeAndFlush(object);
    }

}
