package com.sagarw.project.uber.uberApp.repositories;

import com.sagarw.project.uber.uberApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
