package com.github.chanming2015.domain.sql;

import java.util.Collection;

/**
 * Description:
 * Create Date:2016年12月7日
 * @author XuMaoSen
 * Version:1.0.0
 */
public class SpecRestrictions
{
    public static SpecCriterion isNull(String attributeName)
    {
        return (root, cb) -> cb.isNull(root.get(attributeName));
    }

    public static SpecCriterion isNotNull(String attributeName)
    {
        return (root, cb) -> cb.isNotNull(root.get(attributeName));
    }

    public static SpecCriterion eq(String attributeName, Object value)
    {
        return (root, cb) -> cb.equal(root.get(attributeName), value);
    }

    public static SpecCriterion ne(String attributeName, Object value)
    {
        return (root, cb) -> cb.notEqual(root.get(attributeName), value);
    }

    public static SpecCriterion like(String attributeName, String value)
    {
        return (root, cb) -> cb.like(root.get(attributeName), value);
    }

    public static SpecCriterion notLike(String attributeName, String value)
    {
        return (root, cb) -> cb.notLike(root.get(attributeName), value);
    }

    public static SpecCriterion in(String attributeName, Collection<?> value)
    {
        return (root, cb) -> root.get(attributeName).in(value);
    }

    public static SpecCriterion notIn(String attributeName, Collection<?> value)
    {
        return (root, cb) -> cb.not(root.get(attributeName).in(value));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static SpecCriterion le(String attributeName, Comparable value)
    {
        return (root, cb) -> cb.lessThanOrEqualTo(root.get(attributeName), value);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static SpecCriterion ge(String attributeName, Comparable value)
    {
        return (root, cb) -> cb.greaterThanOrEqualTo(root.get(attributeName), value);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static SpecCriterion between(String attributeName, Comparable lessValue, Comparable greaterValue)
    {
        return (root, cb) -> cb.between(root.get(attributeName), lessValue, greaterValue);
    }

}
