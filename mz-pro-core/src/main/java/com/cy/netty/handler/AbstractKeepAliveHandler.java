package com.cy.netty.handler;

import com.cy.netty.constant.Constants;
import com.cy.netty.entity.proto.CommonHead;
import com.cy.netty.util.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 心跳链接抽象方法
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/9/19
 */
public abstract class AbstractKeepAliveHandler extends ChannelInboundHandlerAdapter {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected CommonHead.KeepAliveRequest req = null;
    protected CommonHead.KeepAliveResponse res = null;
    public static final int BEAT_REQ = 100000;
    public static final int BEAT_RES = 100001;

    /**
     * 最大超时次数
     */
    protected int timeoutNum = 3;

    public int getTimeoutNum() {
        return timeoutNum;
    }

    public void setTimeoutNum(int timeoutNum) {
        this.timeoutNum = timeoutNum;
    }

    public AbstractKeepAliveHandler() {
        //请求定义
        CommonHead.KeepAliveRequest.Builder reqBr = CommonHead.KeepAliveRequest.newBuilder();
        reqBr.setCmd(BEAT_REQ);
        this.req = reqBr.build();

        //响应定义
        CommonHead.KeepAliveResponse.Builder resBr = CommonHead.KeepAliveResponse.newBuilder();
        resBr.setCmd(BEAT_RES);
        this.res = resBr.build();
    }

    /**
     * 心跳处理
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof Request){
            if(((Request) msg).getMessage() instanceof CommonHead.KeepAliveRequest){
                ctx.writeAndFlush(res);
            }
        }
    }

    /**
     * 读写空闲
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if(event.state().equals(IdleState.READER_IDLE)){
                logger.debug("读空闲");
                Integer num = ctx.channel().attr(Constants.TIME_OUT_NUM).get();
                if(num == null || num>timeoutNum){
                    logger.debug("心跳超过{}关闭链接",60);
                    ctx.channel().close();
                }
            } else if(event.state().equals(IdleState.WRITER_IDLE)){
                logger.debug("写空闲");
            } else if(event.state().equals(IdleState.ALL_IDLE)){
                logger.debug("ping");
			    ctx.channel().writeAndFlush(req);
            }
        }
    }

    /**
     * 链接成功心跳计数
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().attr(Constants.TIME_OUT_NUM).set(0);
    }
}
