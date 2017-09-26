package com.cy.netty.codec;

import com.google.protobuf.Message;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 编码解析对象的抽象类
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/9/9
 */
public abstract class MyCoderFactory {

    protected Map<Integer, Class<? extends Message>> cmdClassMap = new ConcurrentHashMap<>();
    protected Map<Integer,Method> cmdMethodMap = new ConcurrentHashMap<>();
    protected Map<Class<? extends Message>,Integer> classCmdMap = new ConcurrentHashMap<>();
    protected boolean initCommand = false;
    protected String fileName = "cmd2proto.properties";

    public abstract void init();

    public abstract void parseMessage();

    public Map<Integer, Class<? extends Message>> getCmdClassMap() {
        return cmdClassMap;
    }

    public Map<Integer, Method> getCmdMethodMap() {
        return cmdMethodMap;
    }

    public Map<Class<? extends Message>, Integer> getClassCmdMap() {
        return classCmdMap;
    }

    public boolean isInitCommand() {
        return initCommand;
    }

    public void setInitCommand(boolean initCommand) {
        this.initCommand = initCommand;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
