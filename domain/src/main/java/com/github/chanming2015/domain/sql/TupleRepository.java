package com.github.chanming2015.domain.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

/**
 * Description:
 * Create Date:2016年12月10日
 * @author XuMaoSen
 * Version:1.0.0
 */
@Repository
public class TupleRepository
{
    @Autowired
    private EntityManager em;

    public <T> List<Tuple> findAll(Class<T> domainClass, SpecProperty... properties)
    {
        return findAll(domainClass, null, null, properties);
    }

    public <T> List<Tuple> findAll(Class<T> domainClass, Collection<SpecProperty> properties)
    {
        return findAll(domainClass, null, null, properties);
    }

    public <T> List<Tuple> findAll(Class<T> domainClass, Specification<T> spec, SpecProperty... properties)
    {
        return findAll(domainClass, spec, null, properties);
    }

    public <T> List<Tuple> findAll(Class<T> domainClass, Specification<T> spec, Collection<SpecProperty> properties)
    {
        return findAll(domainClass, spec, null, properties);
    }

    public <T> List<Tuple> findAll(Class<T> domainClass, Sort sort, SpecProperty... properties)
    {
        return findAll(domainClass, null, sort, properties);
    }

    public <T> List<Tuple> findAll(Class<T> domainClass, Sort sort, Collection<SpecProperty> properties)
    {
        return findAll(domainClass, null, sort, properties);
    }

    public <T> List<Tuple> findAll(Class<T> domainClass, Specification<T> spec, Sort sort, SpecProperty... properties)
    {
        return findAll(domainClass, spec, sort, Arrays.asList(properties));
    }

    public <T> List<Tuple> findAll(Class<T> domainClass, Specification<T> spec, Sort sort, Collection<SpecProperty> properties)
    {
        if ((properties != null) && (properties.size() > 0))
        {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Tuple> query = cb.createTupleQuery();
            Root<T> root = query.from(domainClass);
            List<Selection<?>> selections = new ArrayList<>(properties.size());
            properties.forEach(specProperty -> selections.add(root.get(specProperty.getPropertyName()).alias(specProperty.getAliasName())));
            query.multiselect(selections);
            if (spec != null)
            {
                query.where(spec.toPredicate(root, query, cb));
            }
            if (sort != null)
            {
                query.orderBy(QueryUtils.toOrders(sort, root, cb));
            }
            return em.createQuery(query).getResultList();
        }
        return new ArrayList<>(0);
    }
}
