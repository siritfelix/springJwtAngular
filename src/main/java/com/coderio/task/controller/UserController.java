package com.coderio.task.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coderio.task.controller.dto.ResponseDto;
import com.coderio.task.controller.dto.UserDto;
import com.coderio.task.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Authentication", description = "Authentication catalog")
@RestController
@RequestMapping(path = UserController.PATH)
@RequiredArgsConstructor
public class UserController {
    public static final String PATH = "/users";
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll().stream().map(UserDto::new).toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ResponseDto> removById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.removById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/{id}/{role}")
    public ResponseEntity<UserDto> upDateRole(@PathVariable Integer id, @PathVariable String role) {
        return ResponseEntity.ok(new UserDto(userService.upDateRole(id, role)));
    }
}
