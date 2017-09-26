package com.cy.netty.handler;

import com.cy.netty.constant.Constants;
import com.cy.netty.codec.MyCoder;
import com.cy.netty.codec.MyCoderFactory;
import com.cy.netty.util.Request;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * 解码器
 * +-----+--——--+--——--+--——--+
 * | 长度 | 标示 |  cmd |  内容 |
 * | int |  bit |  int |      |
 * +--——-+--——--+--——--+--——--+
 * 标示&0xE  = 协议类型
 * 标示&0x01 = 协议是否压缩
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/9/9
 */
public class MyDecoderHandler extends ByteToMessageDecoder {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        logger.info("开始粘包=========================");
        // 开始粘包
        if (buffer.readableBytes() < Constants.HEAD_LEN) {
            return;
        }
        buffer.markReaderIndex();
        int length = buffer.readInt(); //长度
        logger.debug("粘包=========================length:{}",length);
        if (buffer.readableBytes() < length) {
            buffer.resetReaderIndex();
            logger.debug("粘包=========================length:return");
            return;
        }
        byte flat = buffer.readByte(); // 标示
        int cmd = buffer.readInt(); // 命令号
        //这里使用零拷贝池内存 处理完释放
        ByteBuf frame = buffer.readSlice(length).retain();//标记这段内存
        boolean compressed = ((flat & Constants.BIT_COMPRESSED) == Constants.BIT_COMPRESSED);//是否压缩
        byte coderFlat = (byte)(flat&0xE); //协议类型
        byte[] dst = new byte[length];
        frame.readBytes(dst);//读入数组
        ReferenceCountUtil.release(frame);//释放标记这段内存

        if(compressed){
            dst = decompress(dst);
        }
        MyCoderFactory media = MyCoder.mediaMap.get(coderFlat);
        if(media == null){
            logger.error("协议类型{}没有对应的转换器",coderFlat);
            return;
        }
        Method method = media.getCmdMethodMap().get(cmd);
        if(method == null){
            logger.error("协议类型{}没有对应的cmd{}转换器",coderFlat,cmd);
            return;
        }
        Object object = method.invoke(null, dst);
        Request request = new Request(cmd,coderFlat,object);
        out.add(request);
        logger.info("粘包完成");
    }

    /**
     * 解压缩
     * @param dst
     * @return
     */
    private byte[] decompress(byte[] dst){
        ByteArrayOutputStream baos = null;
        GZIPInputStream gis = null;
        try{
            baos=new ByteArrayOutputStream();
            gis=new GZIPInputStream(new ByteArrayInputStream(dst));
            byte[] buffer=new byte[1024];
            int n;
            while((n=gis.read(buffer))!=-1){
                baos.write(buffer, 0, n);
            }
            return baos.toByteArray();
        }catch(Exception ex){

        } finally{
            try {
                baos.close();
                if(gis!=null){
                    gis.close();
                }
            } catch (IOException e) {
            }
        }
        return dst;

    }
}
