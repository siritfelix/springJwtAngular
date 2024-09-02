package com.coderio.task.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coderio.task.controller.dto.ResponseDto;
import com.coderio.task.controller.dto.TaskDto;
import com.coderio.task.service.TaskService;
import com.coderio.task.shared.configuration.MenssageResponse;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials = "true")
@Slf4j
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Tacks", description = "Tacks Catalog")
@RequiredArgsConstructor
@RestController
public class TaskController {
	public final static String PATH = "/tasks";
	public final static String FILTER = "/filter";
	private final TaskService taskService;

	@PreAuthorize("hasRole('USER')")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "FindAll Tasks", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskDto.class)))),
			@ApiResponse(responseCode = "403", description = "User  or Task dont'n exists", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
	})
	@GetMapping(PATH)
	public ResponseEntity<List<TaskDto>> findAll(@AuthenticationPrincipal UserDetails userDetails) {
		log.info("----------------------------------------------------------");
		return ResponseEntity
				.ok(taskService.findAll(userDetails.getUsername()).stream().map(TaskDto::new).toList());
	}

	@PreAuthorize("hasRole('USER')")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "FindAll Tasks", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskDto.class)))),
			@ApiResponse(responseCode = "403", description = "User  or Task dont'n exists", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
	})
	@GetMapping(PATH + FILTER)
	public ResponseEntity<List<TaskDto>> findAllByFilter(@AuthenticationPrincipal UserDetails userDetails,
			@Nullable @RequestParam Boolean completada,
			@Nullable @Valid @RequestParam @Pattern(regexp = "^(baja|alta|media)$", message = MenssageResponse.E405) String prioridad) {
		log.info("----------------------------------------------------------{},{}", completada, prioridad);
		return ResponseEntity.ok(
				taskService.findAllByFilter(completada, prioridad, userDetails.getUsername()).stream()
						.map(TaskDto::new).toList());
	}

	@PreAuthorize("hasRole('USER')")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "FindAll Tasks", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseDto.class)))),
			@ApiResponse(responseCode = "403", description = "User  or Task dont'n exists", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
	})
	@PatchMapping(PATH + "/{id}")
	public ResponseEntity<ResponseDto> upDate(@AuthenticationPrincipal UserDetails userDetails,
			@Valid @RequestBody TaskDto taskDto, @PathVariable Integer id) {
		log.info("----------------------------------------------------------{}", taskDto.toString());
		taskDto.setId(id);
		return ResponseEntity.ok(taskService.upDate(taskDto.toTask(), userDetails.getUsername()));
	}

	@PreAuthorize("hasRole('USER')")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "FindAll Tasks", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseDto.class)))),
			@ApiResponse(responseCode = "403", description = "User  or Task dont'n exists", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
	})
	@DeleteMapping(PATH + "/{id}")
	public ResponseEntity<ResponseDto> removeById(@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Integer id) {
		log.info("----------------------------------------------------------{}", id);
		return ResponseEntity.ok(taskService.removeById(id, userDetails.getUsername()));
	}

	@PreAuthorize("hasRole('USER')")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "FindAll Tasks", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseDto.class)))),
			@ApiResponse(responseCode = "403", description = "User  or Task dont'n exists", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
	})
	@PostMapping(PATH)
	public ResponseEntity<ResponseDto> save(@AuthenticationPrincipal UserDetails userDetails,
			@Valid @RequestBody TaskDto taskDto) {
		log.info("----------------------------------------------------------{}", taskDto);
		return ResponseEntity.ok(taskService.save(taskDto.toTask(), userDetails.getUsername()));
	}
}
