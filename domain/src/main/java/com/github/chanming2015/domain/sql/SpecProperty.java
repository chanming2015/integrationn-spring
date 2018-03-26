package com.github.chanming2015.domain.sql;

/**
 * Description:
 * Create Date:2016年12月10日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class SpecProperty
{
    private final String propertyName;
    private String aliasName;

    private SpecProperty(String name)
    {
        propertyName = name;
        aliasName = name;
    }

    public static SpecProperty forName(String name)
    {
        return new SpecProperty(name);
    }

    public SpecProperty alias(String name)
    {
        aliasName = name;
        return this;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public String getAliasName()
    {
        return aliasName;
    }
}
