package test;

import com.cy.netty.boot.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/9/9
 */
public class ServerMainTest {

    private static Logger logger = LoggerFactory.getLogger(ServerMainTest.class);

    public static void main(String[] args) {
        try {
            final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
            NettyServer nettyServer = (NettyServer) context.getBean("nettyServer");
            nettyServer.run();
//            context.start();
        } catch (Exception e) {
            logger.error("== ServerMainTest context start error:", e);
        }
        synchronized (ServerMainTest.class) {
            while (true) {
                try {
                    ServerMainTest.class.wait();
                } catch (InterruptedException e) {
                    logger.error("== synchronized error:", e);
                }
            }
        }
    }
}
