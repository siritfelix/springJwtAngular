package com.coderio.task.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.coderio.task.repository.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
