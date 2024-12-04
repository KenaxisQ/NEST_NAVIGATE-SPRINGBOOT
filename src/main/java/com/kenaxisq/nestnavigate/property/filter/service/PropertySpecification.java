package com.kenaxisq.nestnavigate.property.filter.service;

import com.kenaxisq.nestnavigate.property.entity.Property;
import com.kenaxisq.nestnavigate.property.filter.dto.PropertyFilterDto;
import com.kenaxisq.nestnavigate.user.service.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PropertySpecification {

    private final UserService userService;

    @Autowired
    public PropertySpecification(UserService userService) {
        this.userService = userService;
    }

    public Specification<Property> getPropertiesWithFilters(PropertyFilterDto filterDto) {
        return (Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getUserIds() != null && !filterDto.getUserIds().isEmpty()) {
                predicates.add(root.get("owner").in(userService.getUsersByIds(filterDto.getUserIds())));
            }
            if (filterDto.getRequiredPropertiesIds() != null && !filterDto.getRequiredPropertiesIds().isEmpty()) {
                predicates.add(root.get("id").in(filterDto.getRequiredPropertiesIds()));
            }
            if (filterDto.getType() != null) {
                predicates.add(builder.equal(root.get("type"), filterDto.getType()));
            }
            if (filterDto.getPropertyCategory() != null) {
                predicates.add(builder.equal(root.get("propertyCategory"), filterDto.getPropertyCategory()));
            }
            if (filterDto.getMinPrice() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("price"), filterDto.getMinPrice()));
            }
            if (filterDto.getMaxPrice() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("price"), filterDto.getMaxPrice()));
            }
            if (filterDto.getSearchKeyword() != null && !filterDto.getSearchKeyword().isEmpty()) {
                Predicate titlePredicate = builder.like(root.get("title"), "%" + filterDto.getSearchKeyword() + "%");
                Predicate descriptionPredicate = builder.like(root.get("description"), "%" + filterDto.getSearchKeyword() + "%");
                predicates.add(builder.or(titlePredicate, descriptionPredicate));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}