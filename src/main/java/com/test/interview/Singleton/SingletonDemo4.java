package com.test.interview.Singleton;

/**
 * 饿汉式
 * 优点避免了多线程的同步问题
 * 缺点类装载时就实例化
 */
public class SingletonDemo4 {
    private static SingletonDemo4 singletonDemo4;
    private SingletonDemo4(){

    }
    static {
        singletonDemo4=new SingletonDemo4();
    }
    public static SingletonDemo4 getInstance(){
        return  singletonDemo4;
    }
}
