import com.cy.netty.boot.NettyClient;
import com.cy.netty.entity.proto.CommonHead;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/9/9
 */
public class ClientMainTest {

    private static Logger logger = LoggerFactory.getLogger(ClientMainTest.class);

    public static void main(String[] args) {
        try {
            final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
            NettyClient nettyClient = (NettyClient) context.getBean("nettyClient");
            ChannelFuture f = nettyClient.run();
//            CommonHead.KeepAliveRequest.Builder ch = CommonHead.KeepAliveRequest.newBuilder();
//            ch.setCmd(10000);
//            f.channel().writeAndFlush(ch.build());
//            context.start();
        } catch (Exception e) {
            logger.error("== ClientMainTest context start error:", e);
        }
        synchronized (ClientMainTest.class) {
            while (true) {
                try {
                    ClientMainTest.class.wait();
                } catch (InterruptedException e) {
                    logger.error("== synchronized error:", e);
                }
            }
        }
    }
}
