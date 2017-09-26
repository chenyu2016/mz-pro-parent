package com.cy.netty.boot;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/9/9
 */
public class NettyClient {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 远程地址
     */
    private String host;

    /**
     * 远程端口
     */
    private int port;

    /**
     * handler 执行器
     * spring 注入这个是注意是多例的 scope="prototype"
     */
    private ChannelInitializer<SocketChannel> childChannelHandler;

    private EventLoopGroup workerGroup;

    public ChannelFuture run() throws Exception{
        if(childChannelHandler==null){
            logger.error("客户端启动错误 handler channelInitializer is null");
            return null;
        }
//        try {
            workerGroup = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE,false);
            b.option(ChannelOption.TCP_NODELAY,true);
            b.handler(childChannelHandler);
            ChannelFuture f = b.connect(host, port).sync();
            logger.debug("客户端启动成功链接主机：{}:{}",host,port);
            return f;
//            f.channel().closeFuture().sync();
//        } finally {
//            workerGroup.shutdownGracefully();
//        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ChannelInitializer<SocketChannel> getChildChannelHandler() {
        return childChannelHandler;
    }

    public void setChildChannelHandler(ChannelInitializer<SocketChannel> childChannelHandler) {
        this.childChannelHandler = childChannelHandler;
    }
}
