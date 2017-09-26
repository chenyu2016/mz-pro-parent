package com.cy.netty.util;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 * User: ChenYu
 * Date: 2017/8/15
 *
 * RPC 同步请求
 */
public class DefaultFuture {

//    public static ConcurrentHashMap<Long, DefaultFuture> allDefaultFuture = new ConcurrentHashMap<Long, DefaultFuture>();
//    final Lock lock = new ReentrantLock();
//    private Condition condition = lock.newCondition();
//    private Request request;
//    private Response response;
//
//    public DefaultFuture(Request request) {
//        allDefaultFuture.put(request.getId(),this);
//        this.request = request;
//    }
//
//    public Response getResponse(int time){
//        try{
//            lock.lock();
//            Channel channel = this.request.getChannel();
//            channel.writeAndFlush(JSONObject.toJSONString(request));
//            while(!done()){
//                condition.await(time, TimeUnit.SECONDS);
//            }
//        }catch (Exception e){
//
//        }finally {
//            lock.unlock();
//        }
//        return this.response;
//    }
//
//    public boolean done(){
//        return response!=null;
//    }
//
//    public static void receive(Response response){
//        DefaultFuture df = allDefaultFuture.get(response.getId());
//        if(df!=null){
//            Lock lock = df.lock;
//            lock.lock();
//            try{
//                df.setResponse(response);
//                df.condition.signal();
//            }catch(Exception e){
//
//            }finally{
//                lock.unlock();
//            }
//        }
//    }
//
//    public void setResponse(Response response) {
//        this.response = response;
//    }
}
