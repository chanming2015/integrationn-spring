package com.github.chanming2015.domain.sql;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

/**
 * Description: Create Date:2016年12月7日
 *
 * @author XuMaoSen Version:1.0.0
 */
public class SpecUtil
{
    public static <T> Specification<T> spec(final SpecParam<T> specParam)
    {
        return (root, query, cb) ->
        {
            if ((specParam != null) && (specParam.getCriterions().size() > 0))
            {
                Predicate result = null;
                Collection<SpecCriterion> specs = specParam.getCriterions();
                {
                    Set<Predicate> predicates = new HashSet<>(4);
                    specs.forEach(spec -> predicates.add(spec.getPredicate(root, cb)));
                    result = cb.and(predicates.toArray(new Predicate[predicates.size()]));
                }
                Set<Collection<SpecCriterion>> orSpecSet = specParam.getOrSpecCriterions();
                if ((orSpecSet != null) && (orSpecSet.size() > 0))
                {
                    for (Collection<SpecCriterion> orSpecs : orSpecSet)
                    {
                        Set<Predicate> predicates = new HashSet<>(4);
                        orSpecs.forEach(spec -> predicates.add(spec.getPredicate(root, cb)));
                        Predicate orResult = cb
                                .and(predicates.toArray(new Predicate[predicates.size()]));
                        result = cb.or(result, orResult);
                    }
                }
                return result;
            }
            return cb.conjunction();
        };
    }
}
