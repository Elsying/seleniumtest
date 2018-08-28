package com.test.util.nio;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Unsafe类使Java拥有了像C语言的指针一样操作内存空间的能力
 */
public class UnsafeDemo {

    public void createUnsafe(){
        //创建Unsafed对象
        /*该类有方法：
        内存管理。包括分配内存、释放内存
        非常规的对象实例化allocateInstance()
        操作类、对象、变量
        数组操作
        多线程同步。包括锁机制、CAS操作等
        挂起与恢复
        内存屏障

         */
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe unsafe = (Unsafe) f.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
