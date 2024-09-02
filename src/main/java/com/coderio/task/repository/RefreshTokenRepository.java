package com.coderio.task.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.coderio.task.repository.entity.RefreshToken;
import com.coderio.task.repository.entity.User;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(User user);

    List<RefreshToken> findByUser(User user);
}
