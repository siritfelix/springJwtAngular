package com.coderio.task.shared.exception;

import com.coderio.task.controller.dto.ResponseDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class ConflictException extends RuntimeException {
    private final ResponseDto responseDto;
}
