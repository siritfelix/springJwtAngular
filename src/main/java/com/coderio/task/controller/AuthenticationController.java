package com.coderio.task.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coderio.task.controller.dto.JwtAuthenticationResponse;
import com.coderio.task.controller.dto.ResponseDto;
import com.coderio.task.controller.dto.SignUpRequest;
import com.coderio.task.controller.dto.SigninRequest;
import com.coderio.task.controller.dto.UserInfoDto;
import com.coderio.task.service.AuthenticationService;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Authentication", description = "Authentication catalog")
@RestController
@RequestMapping(path = AuthenticationController.PATH)
@RequiredArgsConstructor
public class AuthenticationController {
    public static final String PATH = "/api/v1/auth/";
    public static final String SINGUP = "signup";
    public static final String SINGIN = "signin";
    public static final String REFRESH = "refresh-token";
    public static final String LOGOUT = "logout";

    private final AuthenticationService authenticationService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register user", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseDto.class)))),
            @ApiResponse(responseCode = "403", description = "User already exists", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping(SINGUP)
    public ResponseEntity<ResponseDto> signup(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticate user", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserInfoDto.class)))),
            @ApiResponse(responseCode = "409", description = "Authenticate fail", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping(SINGIN)
    public ResponseEntity<UserInfoDto> signin(@Valid @RequestBody SigninRequest request) {
        return authenticationService.signin(request);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticate user", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseDto.class)))),
            @ApiResponse(responseCode = "409", description = "Authenticate fail", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping(REFRESH)
    public ResponseEntity<ResponseDto> regreshToken(HttpServletRequest request) {
        return authenticationService.getJwtRefreshFromCookies(request);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticate user", content = @Content(array = @ArraySchema(schema = @Schema(implementation = JwtAuthenticationResponse.class)))),
            @ApiResponse(responseCode = "409", description = "Authenticate fail", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping(LOGOUT)
    public ResponseEntity<ResponseDto> logout(@AuthenticationPrincipal UserDetails userDetails) {
        return authenticationService.logout(userDetails);
    }
}
