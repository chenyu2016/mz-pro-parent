package com.cy.netty.constant;

import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * 常量
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/9/9
 */
public class Constants {

    /** 协议已压缩*/
    public static final byte BIT_COMPRESSED = 0x01;

    /** 协议一般头长度*/
    public static final int HEAD_LEN = 9;

    /** 协议类型AFM3*/
    public static final byte BIT_AMF3 = 0x00;
    /** 协议类型JAVA*/
    public static final byte BIT_JAVA = 0x02;
    /** 协议类型PROTOBUF*/
    public static final byte BIT_PROTOBUF = 0x04;
    /** 协议类型JSON*/
    public static final byte BIT_JSON = 0x06;
    /** 协议类型String*/
//    public static final byte BIT_STRING = 0x08;
    /** 协议压缩是否开启*/
    public static boolean compressEnable = true;
    /** decoder最大长度*/
    public static int MAXDECODELEN = 5 * 1024 * 1024;
    /**
     * 字节数》compressLen压缩，否则不压缩
     */
    public static int compressLen = 5120;

    /**
     * 超时的次数
     */
    public static AttributeKey<Integer> TIME_OUT_NUM = AttributeKey.valueOf("TIME_OUT_NUM");
}
