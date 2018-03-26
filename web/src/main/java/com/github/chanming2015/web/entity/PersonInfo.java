package com.github.chanming2015.web.entity;

import javax.persistence.Entity;

import com.github.chanming2015.domain.entity.BaseEntity;

/**
 * Description:
 * Create Date:2018年3月26日
 * @author XuMaoSen
 * Version:1.0.0
 */
@Entity
public class PersonInfo extends BaseEntity
{
    private static final long serialVersionUID = 8733972530340277879L;

    private String name;
    private String sex;
    private Integer age;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public Integer getAge()
    {
        return age;
    }

    public void setAge(Integer age)
    {
        this.age = age;
    }
}
