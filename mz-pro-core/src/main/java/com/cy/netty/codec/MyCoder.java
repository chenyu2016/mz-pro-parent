package com.cy.netty.codec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 编码解析总类
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/9/9
 */
public class MyCoder {

    private static MyCoder md = null;
    public static Map<Byte,MyCoderFactory> mediaMap = new ConcurrentHashMap<>();

    private MyCoder() {}

    public static MyCoder newInstance() {
        if (md == null) {
            md = new MyCoder();
        }
        return md;
    }

    public Map<Byte, MyCoderFactory> getMediaMap() {
        return mediaMap;
    }

    public void setMediaMap(Map<Byte, MyCoderFactory> mediaMap) {
        MyCoder.mediaMap = mediaMap;
    }
}
