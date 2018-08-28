package com.test.interview.Singleton;

/**
 * 单例模式
 * 懒汉，线程安全
 */
public class SingletonDemo2 {
    private static SingletonDemo2 singletonDemo2;
    private SingletonDemo2(){}
    public static synchronized SingletonDemo2 getInstance(){
        if(singletonDemo2==null){
            singletonDemo2=new SingletonDemo2();
        }
        return singletonDemo2;
    }
}
