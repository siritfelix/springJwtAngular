package com.coderio.task.service.impl;

import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coderio.task.controller.dto.ResponseDto;
import com.coderio.task.controller.dto.SignUpRequest;
import com.coderio.task.controller.dto.SigninRequest;
import com.coderio.task.controller.dto.UserInfoDto;
import com.coderio.task.repository.UserRepository;
import com.coderio.task.repository.entity.RefreshToken;
import com.coderio.task.repository.entity.Role;
import com.coderio.task.repository.entity.User;
import com.coderio.task.service.AuthenticationService;
import com.coderio.task.service.JwtService;
import com.coderio.task.shared.configuration.MenssageResponse;
import com.coderio.task.shared.exception.ConflictException;
import com.coderio.task.shared.exception.TokenRefreshException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final MenssageResponse menssageResponse;

	@Override
	public ResponseDto signup(SignUpRequest request) {

		var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
				.email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
				.role(Role.valueOf(request.getRole()))
				.build();
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new ConflictException(ResponseDto.builder()
					.code(MenssageResponse.E409)
					.message(menssageResponse.getMessages().get(MenssageResponse.E409)
							.concat(request.getEmail()))
					.build());
		}
		userRepository.save(user);
		return ResponseDto.builder()
				.code(MenssageResponse.OK)
				.message(menssageResponse.getMessages().get(MenssageResponse.OK))
				.build();
	}

	@Override
	public ResponseEntity<UserInfoDto> signin(SigninRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
		RefreshToken refreshToken = jwtService.createRefreshToken(user);
		ResponseCookie jwtCookie = jwtService.generateJwtCookie(user);
		ResponseCookie jwtRefreshCookie = jwtService.generateRefreshJwtCookie(refreshToken.getToken());

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
				.body(UserInfoDto.builder().firstName(user.getFirstName()).lastName(user.getLastName())
						.email(user.getEmail())
						.role(user.getRole().name())
						.build());
	}

	@Override
	public ResponseEntity<ResponseDto> getJwtRefreshFromCookies(HttpServletRequest request) {
		String refreshToken = jwtService.getJwtRefreshFromCookies(request);

		if ((refreshToken != null) && (refreshToken.length() > 0)) {
			return jwtService.findByToken(refreshToken)
					.map(jwtService::verifyExpiration)
					.map(RefreshToken::getUser)
					.map(user -> {
						ResponseCookie jwtCookie = jwtService.generateJwtCookie(user);
						return ResponseEntity.ok()
								.header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
								.body(menssageResponse.getResponseDtoByCode(MenssageResponse.OK_RT));
					})
					.orElseThrow(() -> new TokenRefreshException(
							menssageResponse.getResponseDtoByCode(MenssageResponse.S002)));
		}
		return ResponseEntity.badRequest().body(menssageResponse.getResponseDtoByCode(MenssageResponse.S003));
	}

	@Override
	public ResponseEntity<ResponseDto> logout(UserDetails userDetails) {
		if (Objects.nonNull(userDetails)) {
			String username = userDetails.getUsername();
			User user = userRepository.findByEmail(username).orElseThrow(() -> new ConflictException(null));
			log.info("Cerrando seccion");
			jwtService.deleteByUserId(user);

		}

		ResponseCookie jwtCookie = jwtService.getCleanJwtCookie();
		ResponseCookie jwtRefreshCookie = jwtService.getCleanJwtRefreshCookie();

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
				.body(menssageResponse.getResponseDtoByCode(MenssageResponse.OK_LOGOUT));
	}

}
