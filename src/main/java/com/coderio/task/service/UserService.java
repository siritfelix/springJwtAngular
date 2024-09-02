package com.coderio.task.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.coderio.task.controller.dto.ResponseDto;
import com.coderio.task.repository.entity.User;

public interface UserService {

    UserDetailsService userDetailsService();

    List<User> findAll();

    ResponseDto removById(Integer id);

    User upDateRole(Integer id, String rol);
}
