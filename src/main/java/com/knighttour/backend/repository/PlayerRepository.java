package com.knighttour.backend.repository;

import com.knighttour.backend.model.Player;
import com.knighttour.backend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
