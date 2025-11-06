package com.agri_supplies_shop.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@AllArgsConstructor
public class BaseSpecification<T> implements Specification<T> {
    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            if (criteria.getValue().getClass() == LocalDate.class) {
                return builder.greaterThanOrEqualTo(
                        root.get(criteria.getKey()),
                        (LocalDate) criteria.getValue()
                );
            } else
                return builder.greaterThanOrEqualTo(
                        root.<String>get(criteria.getKey()),
                        criteria.getValue().toString()
                );
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            if (criteria.getValue().getClass() == LocalDate.class) {
                return builder.lessThanOrEqualTo(
                        root.get(criteria.getKey()), (LocalDate) criteria.getValue()
                );
            } else
                return builder.lessThanOrEqualTo(
                        root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            return builder.like(
                    root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
        } else if (criteria.getOperation().equalsIgnoreCase("=") && criteria.getNameObject() != null
                && criteria.getNameTableJoin() != null
        ) {
            return builder.equal(root.join(criteria.getNameTableJoin()).get(criteria.getNameObject())
                            .get(criteria.getKey())
                    , criteria.getValue());
        } else if (criteria.getOperation().equalsIgnoreCase("=") && criteria.getNameObject() != null) {
            return builder.equal(root.get(criteria.getNameObject()).get(criteria.getKey()), criteria.getValue());
        } else if (criteria.getOperation().equalsIgnoreCase("=")) {
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        }
        return null;
    }
}
