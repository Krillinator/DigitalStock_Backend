package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.repository;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CustomUser, Long> {

    // Queries
    Optional<CustomUser> findByUsername(String username);

}
