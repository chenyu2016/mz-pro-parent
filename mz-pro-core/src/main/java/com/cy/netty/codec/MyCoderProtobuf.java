package com.cy.netty.codec;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * 编码解析城protobuf对象
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/9/9
 */
public class MyCoderProtobuf extends MyCoderFactory {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(){
        parseMessage();
    }

    @Override
    public void parseMessage() {
        if(initCommand) {
            logger.debug("配置文件已经初始化");
            return;
        }
        logger.debug("开始初始化{}",fileName);
        Properties properties = new Properties();
//        String dir = System.getProperty("user.dir") + File.separator + "conf" + File.separator + fileName;
        FileInputStream in = null;
        try {
            File file1 = ResourceUtils.getFile("classpath:"+getFileName());
//            in = new FileInputStream(getFileName());
            in = new FileInputStream(file1.getPath());
            properties.load(in);
            initCommand = true;
        } catch (Exception e) {
            logger.error("初始化{}有错",fileName);
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if(properties==null){
            return;
        }
        Iterator it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            try {
                Class<Message> msgClass = (Class<Message>) Class
                        .forName((String) value);
                int cmd = Integer.parseInt(key.toString());
                classCmdMap.put(msgClass, cmd);
                cmdClassMap.put(cmd, msgClass);
                Class[] paramsTypes = { byte[].class };
                Method method = msgClass.getMethod("parseFrom",paramsTypes);
                cmdMethodMap.put(cmd, method);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        initCommand = true;
    }


}
