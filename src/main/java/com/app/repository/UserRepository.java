package com.app.repository;

import com.app.model.Role;
import com.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<Boolean> existsUserByEmail(String email);
    List<User> findAllByRolesIsContaining(Role role);
}
