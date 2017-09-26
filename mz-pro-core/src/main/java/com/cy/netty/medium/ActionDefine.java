package com.cy.netty.medium;

import java.lang.reflect.Method;

/**
 * 执行器的抽象类
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/9/9
 */
public abstract class ActionDefine {

    /**
     * 命令号
     */
    protected int cmd;

    /**
     * 对应的类
     */
    protected Object bean;

    /**
     * 对应类的执行方法
     */
    private String methodName;

    private Method method;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
