package com.cy.netty.medium;

import com.cy.netty.util.Request;
import com.cy.netty.util.Response;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/9/9
 */
public class Media implements ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static Media ourInstance = new Media();

    public static Media getInstance() {
        return ourInstance;
    }

    private Media() {
    }

    private static Map<Integer, ActionDefine> actions = new HashMap<Integer, ActionDefine>();
    private static List<ActionDefine> actionDefines;


    /**
     * 反射处理结果
     *
     * @param request
     * @return
     */
    public Object process(Request request, Channel channel) {

        logger.debug("反射处理结果");
        Object obj = null;
        ActionDefine af = Media.actions.get(request.getCmd());
        if (af == null) {
            logger.error("请求异常");
            return null;
        }
        try {
            Response response = new Response(channel);
            obj = af.getMethod().invoke(af.getBean(), response);
        } catch (Exception e) {
            logger.error("反射处理结果 失败：" + e.getMessage());
        }
        return obj;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        if (actionDefines == null) {
            actionDefines = new ArrayList<>();

            while (ctx != null) {
                Map<String, ActionDefine> maps = ctx
                        .getBeansOfType(ActionDefine.class);
                if (maps.values() != null && maps.values().size() > 0) {
                    actionDefines.addAll(maps.values());
                }
                ctx = ctx.getParent();

            }
        }
        if (actionDefines != null) {
            // 初始化ActionDefine

            Class[] paramsType = {Object.class, Response.class};
            for (ActionDefine ad : actionDefines) {
                int cmd = ad.getCmd();
                if (actions.containsKey(cmd)) {
//                    throw new BaseException("重复的命令号:" + cmd);
                    logger.error("重复的命令号:" + cmd);
                }

                Method m = null;
                try {
                    m = ad.getBean().getClass().getDeclaredMethod(
                            ad.getMethodName(), paramsType);
                } catch (Exception e) {
                    logger.error(
                            "action mapping method is error : cmd={}, method={}",
                            cmd, ad.getMethodName());
                }
                if (m == null) {
                    continue;
                }
                ad.setMethod(m);

                actions.put(cmd, ad);
            }
            actionDefines.clear();
            actionDefines = null;
        }
    }
}
