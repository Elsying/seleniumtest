package com.test.interview.Singleton;

/**
 * 饿汉式
 * 优点避免了多线程的同步问题
 * 缺点类装载时就实例化
 */
public class SingletonDemo3 {
    private static SingletonDemo3 singletonDemo3=new SingletonDemo3();
    private SingletonDemo3(){

    }
    public static SingletonDemo3 getInstance(){
        return singletonDemo3;
    }
}
