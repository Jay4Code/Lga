package com.lga.util;

/**
 * Created by Jay.X
 *
 * 静态内部类单例模式
 *
 * 这种方式只有在的第一次调用getInstance()方法时，
 * 虚拟机才会加载SingletonHolder类，并初始化instance实例，
 * 即保证了线程同步，也能保证单例的唯一性。
 */
public class Singleton {

    private static class SingletonHolder {
        private static final Singleton instance = new Singleton();
    }

    private Singleton() {}

    public static Singleton getInstance() {
        return SingletonHolder.instance;
    }
}