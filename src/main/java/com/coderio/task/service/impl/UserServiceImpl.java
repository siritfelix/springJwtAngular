package com.coderio.task.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.coderio.task.controller.dto.ResponseDto;
import com.coderio.task.repository.UserRepository;
import com.coderio.task.repository.entity.Role;
import com.coderio.task.repository.entity.User;
import com.coderio.task.service.UserService;
import com.coderio.task.shared.configuration.MenssageResponse;
import com.coderio.task.shared.exception.ConflictException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MenssageResponse menssageResponse;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        Iterator<User> iterator = userRepository.findAll().iterator();
        while (iterator.hasNext()) {
            users.add(iterator.next());
        }
        return users;
    }

    @Override
    public ResponseDto removById(Integer id) {
        userRepository.deleteById(id);
        return menssageResponse.getResponseDtoByCode(MenssageResponse.OK);
    }

    @Override
    public User upDateRole(Integer id, String rol) {
        User user = userRepository.findById(id).orElseThrow(() -> new ConflictException(ResponseDto.builder().build()));
        user.setRole(Role.valueOf(rol));
        return userRepository.save(user);
    }
}
