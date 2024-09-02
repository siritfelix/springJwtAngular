package com.coderio.task.shared.exception;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.coderio.task.controller.dto.ResponseDto;
import com.coderio.task.shared.configuration.MenssageResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ControllerAdvice
public class TaskExceptionHandler {
	private final MenssageResponse menssageResponse;

	@ExceptionHandler({ MissingServletRequestParameterException.class })
	public ResponseEntity<ResponseDto> badRequest(MissingServletRequestParameterException e) {
		return new ResponseEntity<>(ResponseDto.builder()
				.code(MenssageResponse.BR400)
				.message(menssageResponse.getMessages().get(MenssageResponse.BR400))
				.build(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ HandlerMethodValidationException.class, MethodArgumentTypeMismatchException.class })
	public ResponseEntity<ResponseDto> badRequest(Exception e) {
		return new ResponseEntity<>(ResponseDto.builder()
				.code(MenssageResponse.BR400)
				.message(e.getLocalizedMessage())
				.build(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<ResponseDto> badRequest(HttpServletRequest request, ConstraintViolationException e) {

		return new ResponseEntity<>(ResponseDto.builder().code(MenssageResponse.BR400).message(
				e.getConstraintViolations().stream().map(c -> c.getMessage())
						.collect(Collectors.joining(MenssageResponse.SP)))
				.build(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<ResponseDto> badRequest(HttpServletRequest request,
			MethodArgumentNotValidException e) {
		return new ResponseEntity<>(buidlResponseDto(e.getBindingResult().getFieldErrors()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ NotFoundException.class })
	public ResponseEntity<ResponseDto> notFound(HttpServletRequest request,
			NotFoundException e) {
		return new ResponseEntity<>(e.getResponseDto(),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ ConflictException.class })
	public ResponseEntity<ResponseDto> conflict(HttpServletRequest request,
			ConflictException e) {
		return new ResponseEntity<>(e.getResponseDto(),
				HttpStatus.CONFLICT);
	}

	@ExceptionHandler({ UsernameNotFoundException.class, ServletException.class, IOException.class,
			BadCredentialsException.class })
	public ResponseEntity<ResponseDto> usernameNotFoundException(HttpServletRequest request, Exception exception) {
		return new ResponseEntity<>(ResponseDto.builder()
				.code(MenssageResponse.E403)
				.message(menssageResponse.getMessages().get(MenssageResponse.E403))
				.build(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<ResponseDto> fatalErrorUnexpectedException(HttpServletRequest request, Exception exception) {
		return new ResponseEntity<>(ResponseDto.builder()
				.code(MenssageResponse.E500)
				.message(menssageResponse.getMessages().get(MenssageResponse.E500))
				.build(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ ExpiredJwtException.class })
	public ResponseEntity<ResponseDto> expiredJwtException(HttpServletRequest request, Exception exception) {
		return new ResponseEntity<>(ResponseDto.builder()
				.code(MenssageResponse.S001)
				.message(menssageResponse.getMessages().get(MenssageResponse.S001))
				.build(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(value = TokenRefreshException.class)
	public ResponseEntity<ResponseDto> handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
		return new ResponseEntity<>(ResponseDto.builder()
				.code(MenssageResponse.S001)
				.message(menssageResponse.getMessages().get(MenssageResponse.S001))
				.build(), HttpStatus.UNAUTHORIZED);

	}

	private ResponseDto buidlResponseDto(List<FieldError> fieldErrors) {
		return ResponseDto.builder()
				.code(getCode(fieldErrors))
				.message(getMessage(fieldErrors))
				.build();
	}

	private String getCode(List<FieldError> fieldErrors) {
		if (fieldErrors.isEmpty()) {
			return MenssageResponse.BR400;
		}
		return fieldErrors.stream().map(field -> {
			if (Objects.nonNull(field.getDefaultMessage())) {
				return field.getDefaultMessage();
			} else {
				return null;
			}
		}).filter(mss -> mss != null && !mss.isEmpty()).collect(Collectors.joining(MenssageResponse.SP));
	}

	private String getMessage(List<FieldError> fieldErrors) {

		if (fieldErrors.isEmpty()) {
			return menssageResponse.getMessages().get(MenssageResponse.BR400);
		}
		return fieldErrors.stream().map(field -> {
			if (Objects.nonNull(field.getDefaultMessage())) {
				return menssageResponse.getMessages().get(field.getDefaultMessage());
			} else {
				return null;
			}
		}).filter(mss -> mss != null && !mss.isEmpty()).collect(Collectors.joining(MenssageResponse.SP));
	}
}
