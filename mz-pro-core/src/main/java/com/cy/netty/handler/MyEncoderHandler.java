package com.cy.netty.handler;

import com.cy.netty.codec.MyCoder;
import com.cy.netty.constant.Constants;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 * 编码器
 * +-----+--——--+--——--+--——--+
 * | 长度 | 标示 |  cmd |  内容 |
 * | int |  bit |  int |      |
 * +--——-+--——--+--——--+--——--+
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/9/9
 */
public class MyEncoderHandler extends MessageToByteEncoder<Object> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        logger.debug("encode=============",msg.toString());
        byte[] b = null;
        byte flag;
        int cmd = 0;
        if(msg instanceof String){
            b = msg.toString().getBytes();
            flag = Constants.BIT_JSON;
            cmd = 0;//???
        } else if(msg instanceof com.google.protobuf.Message) {
            b = ((Message)msg).toByteArray();
            flag = Constants.BIT_PROTOBUF;
            cmd = MyCoder.mediaMap.get(flag).getClassCmdMap().get(msg.getClass());
        } else{
            logger.error("encode=============编码格式不正确",msg.toString());
            return;
        }
        if(b.length>Constants.compressLen){
            b = compress(b);
            flag = (byte) (flag | Constants.BIT_COMPRESSED);
        }
        out.writeInt(b.length);
        out.writeByte(flag);
        out.writeInt(cmd);
        out.writeBytes(b);
        logger.debug("encode==========发送完成{}",b.length);
    }

    private byte[] compress(byte[] bytes){
        if(bytes==null){
            return new byte[0];
        }
        if(bytes.length<=0 || !Constants.compressEnable){
            return bytes;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            logger.debug("压缩前："+bytes.length);
            gzip = new GZIPOutputStream(out);
            gzip.write(bytes);
            gzip.flush();
            gzip.close();
            logger.debug("压缩后："+out.toByteArray().length);
            return out.toByteArray();
        } catch (Exception e) {
            logger.error("gzip compress error.", e);
        }  finally{
            try {
                if(gzip!=null){
                    gzip.close();
                }
            } catch (IOException e) {
            }
        }
        return bytes;
    }
}
