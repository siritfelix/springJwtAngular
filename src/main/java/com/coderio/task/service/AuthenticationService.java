package com.coderio.task.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import com.coderio.task.controller.dto.ResponseDto;
import com.coderio.task.controller.dto.SignUpRequest;
import com.coderio.task.controller.dto.SigninRequest;
import com.coderio.task.controller.dto.UserInfoDto;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    ResponseDto signup(SignUpRequest request);

    ResponseEntity<UserInfoDto> signin(SigninRequest request);

    ResponseEntity<ResponseDto> getJwtRefreshFromCookies(HttpServletRequest request);

    ResponseEntity<ResponseDto> logout(UserDetails userDetails);

}
