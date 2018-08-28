package com.test.interview.Singleton;

/**
 * 饿汉式（静态内部类）
 * singletonDemo5不一定被初始化。因为SingletonHolder类没有被主动使用，
 * 只有显示通过调用getInstance方法时，才会显示装载SingletonHolder类，从而实例化singletonDemo5
 */
public class SingletonDemo5 {
    private static class SingletonHolder{
        private static final SingletonDemo5 singletonDemo5=new SingletonDemo5();//final修饰的对象只能指向唯一一个对象
    }
    private SingletonDemo5(){
    }
    public static SingletonDemo5 getInsatance(){
        return SingletonHolder.singletonDemo5;
    }
}
