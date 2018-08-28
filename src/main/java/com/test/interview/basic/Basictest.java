package com.test.interview.basic;

import org.junit.Test;
import java.math.BigInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Basictest {
    @Test
    public void Switchtest(){
        //long double float 不能使用switch
        int a =5;
        String code="";
        switch (a) {
            case 1:
                code = "one";
                break;
            case 2:
                code = "two";
                break;
            case 3:
                code = "three";
                break;
            default:
                code = "no";
        }
        System.out.println(code);
        //
        short b=1;
        b= (short) (b+1);//b+1会转为int  当算术表达式包含多个基本数据类型时，整个算术表达式的数据类型会在数据运算时出现类型的自动提升 。所有的 byte ， short ， char 会自动提升为int类型。
        b+=b;//+=运算符Java有自动转换机制，java编译器会对其进行特殊处理
        //
    }

    @Test
    public void chartest(){
        //char转int int转char char采用Unicode编码
        char three='1';
        int num=three;//得到Unicode
        int nums=three-'0';//技巧得到这这数字，因为连续的
        System.out.println(three + ": " + num);
        System.out.println(three + ": " + nums);
    }

    public void mathtest(){
        //最快效率计算
        int a=2*8;
        int b=2<<8;//位运算cpu直接支持的，效率最高  将一个数左移n位，就相当于乘以了2的n次方
       // java.math.BigInteger 类提供操作类似所有Java的基本整数运算符和java.lang.Math中的所有相关的方法
        //ReentrantLock l
    }

    /**
    final
     对于一个final变量，如果是基本数据类型的变量，则其数值一旦在初始化之后便不能更改；如果是引用类型的变量，则在对其初始化之后便不能再让其指向另一个对象。
     **/
    }

