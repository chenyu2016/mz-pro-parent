package com.cy.netty.boot;

import com.cy.netty.util.NamingThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端启动
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/9/9
 */
public class NettyServer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * boss线程数 默认cpu盒+1
     */
    private int bossPoolSize = Runtime.getRuntime().availableProcessors()  + 1;

    /**
     * worker线程数 默认cpu盒*24+1
     */
    private int workerPoolSize = Runtime.getRuntime().availableProcessors() * 24  + 1;

    /**
     * handler 执行器
     * spring 注入这个是注意是多例的 scope="prototype"
     */
    private ChannelInitializer<SocketChannel> childChannelHandler;

    /**
     * 端口
     */
    private int port = 8080;

    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;

    /**
     * 服务器运行
     * @throws Exception
     */
    public void run() throws Exception{
        if(childChannelHandler==null){
            logger.error("服务器启动错误 handler channelInitializer is null");
            return;
        }
        bossGroup = new NioEventLoopGroup(bossPoolSize,new NamingThreadFactory("bossThreadGroup"));
        workerGroup = new NioEventLoopGroup(workerPoolSize,new NamingThreadFactory("workerThreadGroup"));
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup,workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);//使用池内存
            serverBootstrap.option(ChannelOption.SO_BACKLOG,128);
            serverBootstrap.childOption(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT);//使用池内存
            serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);//关闭Nagle算法 有数据立马发送
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, false);//关闭心跳 使用自己维护的心跳
            serverBootstrap.childHandler(childChannelHandler);
//            serverBootstrap.childHandler(new MyChannelHandler());
            ChannelFuture cf = serverBootstrap.bind(port).sync();
            cf.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public int getBossPoolSize() {
        return bossPoolSize;
    }

    public void setBossPoolSize(int bossPoolSize) {
        this.bossPoolSize = bossPoolSize;
    }

    public int getWorkerPoolSize() {
        return workerPoolSize;
    }

    public void setWorkerPoolSize(int workerPoolSize) {
        this.workerPoolSize = workerPoolSize;
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
