package com.test.interview.Singleton;

/**
 * 双重检查锁定
 * 在JDK1.5之后，双重检查锁定才能够正常达到单例效果
 */
public class SingletonDemo7 {
    private volatile static SingletonDemo7 singletonDemo7;
    private SingletonDemo7(){

    }
    public static SingletonDemo7 getSingletonDemo7(){
        if (singletonDemo7==null){
            synchronized (SingletonDemo7.class){
                if (singletonDemo7==null){
                    singletonDemo7=new SingletonDemo7();
                }
            }
        }
        return singletonDemo7;
    }
}
