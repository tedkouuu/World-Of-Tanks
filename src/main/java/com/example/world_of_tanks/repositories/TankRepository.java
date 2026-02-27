package com.example.world_of_tanks.repositories;

import com.example.world_of_tanks.models.Tank;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TankRepository extends JpaRepository<Tank, Long>,
        JpaSpecificationExecutor<Tank> {

    Optional<Tank> findByName(String name);

    @EntityGraph(attributePaths = {"user", "category"})
    List<Tank> findByUserUsername(String ownerUsername);

    @EntityGraph(attributePaths = {"user", "category"})
    List<Tank> findByUserUsernameNot(String noOwnerUsername);

    @EntityGraph(attributePaths = {"user", "category"})
    List<Tank> findByOrderByHealthDesc();

    @Override
    @EntityGraph(attributePaths = {"user", "category"})
    List<Tank> findAll();

}

