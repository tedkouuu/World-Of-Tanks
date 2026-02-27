package com.example.world_of_tanks.repositories;

import com.example.world_of_tanks.models.Tank;
import com.example.world_of_tanks.models.dto.SearchTankDTO;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TankSpecification implements Specification<Tank> {

    private final SearchTankDTO searchTankDTO;

    public TankSpecification(SearchTankDTO searchTankDTO) {
        this.searchTankDTO = searchTankDTO;
    }

    @Override
    public Predicate toPredicate(Root<Tank> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        Predicate p = cb.conjunction();

        String name = searchTankDTO.getName();
        if (name != null && !name.trim().isEmpty()) {
            p.getExpressions().add(
                    cb.like(cb.lower(root.get("name")), "%" + name.trim().toLowerCase() + "%")
            );
        }

        Integer health = searchTankDTO.getHealthAsInteger();
        if (health != null) {
            p.getExpressions().add(
                    cb.greaterThanOrEqualTo(root.get("health"), (long) health)
            );
        }

        Integer power = searchTankDTO.getPowerAsInteger();
        if (power != null) {
            p.getExpressions().add(
                    cb.lessThanOrEqualTo(root.get("power"), (long) power)
            );
        }

        return p;
    }
}
