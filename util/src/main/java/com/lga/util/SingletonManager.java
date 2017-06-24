package com.lga.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jay.X
 *
 * 容器单例模式
 *
 * 采用Map集合管理对象的实例，
 * 保证实例的唯一性，
 * 这种方式多用于管理多种类的实例场景，
 * 同时你的类并不一定需要实现单例机制，
 * 因为SingletonManager可以解决这个问题。
 * 你只需在初始化时创建对应类的实例并调用
 * addInstance(String key, Object instance)来进行保存，
 * 使用时调用getInstance(String key)，
 * 即可根据key得到对应类的实例。
 */
public class SingletonManager {
    private static Map<String, Object> instanceMap = new HashMap<>();

    private SingletonManager() {
    }

    public static void addInstance(String key, Object instance) {
        if (!instanceMap.containsKey(key)) {
            instanceMap.put(key, instance);
        }
    }

    public Object getInstance(String key) {
        return instanceMap.get(key);
    }
}