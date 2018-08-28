package com.test.interview.Singleton;

/**
 * 单例模式
 * 懒汉，线程不安全
 */
public class SingletonDemo1 {
    private static SingletonDemo1 singletonDemo1;
    private SingletonDemo1(){}
    public static SingletonDemo1 getInstance(){
        if(singletonDemo1==null){
            singletonDemo1=new SingletonDemo1();
        }
        return singletonDemo1;
    }

}
