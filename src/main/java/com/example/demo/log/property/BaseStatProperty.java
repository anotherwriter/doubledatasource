package com.example.demo.log.property;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dong hao (donghao04@baidu.com)
 */
public class BaseStatProperty {

    /**
     * client - server
     */
    @Getter
    private AtomicLong requestNum;
    @Getter
    private AtomicLong successNum;
    @Getter
    private AtomicLong exceptionNum;
    @Getter
    private AtomicLong errorNum;
    @Getter
    private AtomicLong rspTimeMs;
    @Getter
    private AtomicLong goodsNum;

    /**
     * server - db
     */
    @Getter
    private AtomicLong dbRequestNum;
    @Getter
    private AtomicLong dbSuccessNum;
    @Getter
    private AtomicLong dbRspTimeMs;

    public BaseStatProperty(){
        requestNum = new AtomicLong(0);
        successNum = new AtomicLong(0);
        exceptionNum = new AtomicLong(0);
        errorNum = new AtomicLong(0);
        rspTimeMs = new AtomicLong(0);
        dbRequestNum = new AtomicLong(0);
        dbSuccessNum = new AtomicLong(0);
        dbRspTimeMs = new AtomicLong(0);
        goodsNum = new AtomicLong(0);
    }

    public void reset(){
        requestNum.set(0);
        successNum.set(0);
        exceptionNum.set(0);
        errorNum.set(0);
        rspTimeMs.set(0);
        dbRequestNum.set(0);
        dbSuccessNum.set(0);
        dbRspTimeMs.set(0);
        goodsNum.set(0);
    }

    public void addRequestNum(){
        requestNum.incrementAndGet();
    }

    public void addSuccessNum(){
        successNum.incrementAndGet();
    }

    public void addExceptionNum(){
        exceptionNum.incrementAndGet();
    }

    public void addErrorNum(){
        errorNum.incrementAndGet();
    }

    public void addRspTimeMs(long delta){
        rspTimeMs.addAndGet(delta);
    }

    public void addDbRequestNum(){
        dbRequestNum.incrementAndGet();
    }

    public void addDbRequestNum(long num){
        dbRequestNum.addAndGet(num);
    }

    public void addDbSuccessNum(){
        dbSuccessNum.incrementAndGet();
    }

    public void addDbSuccessNum(long num){
        dbSuccessNum.addAndGet(num);
    }

    public void addDbRspTimeMs(long delta){
        dbRspTimeMs.addAndGet(delta);
    }

    public void addGoodsNum(long delta){
        goodsNum.addAndGet(delta);
    }
}
