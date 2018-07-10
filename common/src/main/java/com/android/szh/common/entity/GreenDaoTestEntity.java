package com.android.szh.common.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by sunzhonghao on 2018/7/10.
 * desc:用于greenDao测试的实体对象，会生成对应的表。
 */

@Entity
public class GreenDaoTestEntity {
    //不能用int
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private int age;
    @Generated(hash = 50767516)
    public GreenDaoTestEntity(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    @Generated(hash = 1995925956)
    public GreenDaoTestEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "GreenDaoTestEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
