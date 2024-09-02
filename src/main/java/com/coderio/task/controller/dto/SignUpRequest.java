package com.coderio.task.controller.dto;

import com.coderio.task.shared.configuration.MenssageResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @Schema(description = "First name,", example = "firstName", required = false)
    private String firstName;

    @Schema(description = "Last name,", example = "lastName", required = false)
    private String lastName;

    @Schema(description = "Email, Unique identifier,", example = "email@email1.com", required = true)
    @Email(message = MenssageResponse.BR400)
    @NotBlank(message = MenssageResponse.BR400)
    private String email;

    @Schema(description = "password,", example = "12345", required = true)
    @NotBlank(message = MenssageResponse.BR400)
    private String password;

    @Schema(description = "role,", example = "USER", required = false)
    private String role;
}
