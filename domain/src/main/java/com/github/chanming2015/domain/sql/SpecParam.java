package com.github.chanming2015.domain.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

/**
 * Description:
 * Create Date:2016年12月7日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class SpecParam<T>
{
    private final Map<String, SpecCriterion> specCriterions = new HashMap<String, SpecCriterion>(4);
    private Set<Collection<SpecCriterion>> orSpecCriterions = null;

    public SpecParam<T> isNull(String fieldName)
    {
        if (!StringUtils.isEmpty(fieldName))
        {
            specCriterions.put(fieldName, SpecRestrictions.isNull(fieldName));
        }
        return this;
    }

    public SpecParam<T> isNotNull(String fieldName)
    {
        if (!StringUtils.isEmpty(fieldName))
        {
            specCriterions.put(fieldName, SpecRestrictions.isNotNull(fieldName));
        }
        return this;
    }

    public SpecParam<T> eq(String fieldName, Object value)
    {
        if (!StringUtils.isEmpty(fieldName) && (null != value))
        {
            specCriterions.put(fieldName, SpecRestrictions.eq(fieldName, value));
        }
        return this;
    }

    public SpecParam<T> ne(String fieldName, Object value)
    {
        if (!StringUtils.isEmpty(fieldName) && (null != value))
        {
            specCriterions.put(fieldName, SpecRestrictions.ne(fieldName, value));
        }
        return this;
    }

    public SpecParam<T> like(String fieldName, String value)
    {
        if (!StringUtils.isEmpty(fieldName) && !StringUtils.isEmpty(value))
        {
            specCriterions.put(fieldName, SpecRestrictions.like(fieldName, value));
        }
        return this;
    }

    public SpecParam<T> notLike(String fieldName, String value)
    {
        if (!StringUtils.isEmpty(fieldName) && !StringUtils.isEmpty(value))
        {
            specCriterions.put(fieldName, SpecRestrictions.notLike(fieldName, value));
        }
        return this;
    }

    public SpecParam<T> in(String fieldName, Collection<?> value) {
        if (!StringUtils.isEmpty(fieldName) && (null != value) && (value.size() > 0))
        {
            specCriterions.put(fieldName, SpecRestrictions.in(fieldName, value));
        }
        return this;
    }

    public SpecParam<T> notIn(String fieldName, Collection<?> value)
    {
        if (!StringUtils.isEmpty(fieldName) && (null != value) && (value.size() > 0))
        {
            specCriterions.put(fieldName, SpecRestrictions.notIn(fieldName, value));
        }
        return this;
    }

    public SpecParam<T> le(String fieldName, Comparable<?> value)
    {
        if (!StringUtils.isEmpty(fieldName) && (null != value))
        {
            specCriterions.put(fieldName, SpecRestrictions.le(fieldName, value));
        }
        return this;
    }

    public SpecParam<T> ge(String fieldName, Comparable<?> value)
    {
        if (!StringUtils.isEmpty(fieldName) && (null != value))
        {
            specCriterions.put(fieldName, SpecRestrictions.ge(fieldName, value));
        }
        return this;
    }

    public <Y> SpecParam<T> between(String fieldName, Comparable<? extends Y> lessValue, Comparable<? extends Y> greaterValue)
    {
        if (!StringUtils.isEmpty(fieldName) && (null != lessValue) && (null != greaterValue))
        {
            specCriterions.put(fieldName, SpecRestrictions.between(fieldName, lessValue, greaterValue));
        }
        return this;
    }

    public SpecParam<T> or(SpecParam<T> param)
    {
        if (null != param)
        {
            Collection<SpecCriterion> specCriterions = param.getCriterions();
            if (specCriterions.size() > 0)
            {
                if (null == orSpecCriterions)
                {
                    orSpecCriterions = new HashSet<Collection<SpecCriterion>>(4);
                }
                orSpecCriterions.add(specCriterions);
            }
        }
        return this;
    }

    public Collection<SpecCriterion> getCriterions()
    {
        return specCriterions.values();
    }

    public Set<Collection<SpecCriterion>> getOrSpecCriterions()
    {
        return orSpecCriterions;
    }
}
