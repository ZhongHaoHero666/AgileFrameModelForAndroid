package com.android.szh.common.entity;

import java.io.Serializable;

/**
 * Created by sunzhonghao on 2018/7/10.
 * desc:用于演示Aroute 组件通信的实体对象
 */

public class ARouteSerializableBean implements Serializable {
    private String name;
    private int age;

    public ARouteSerializableBean(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "ARouteSerializableBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
