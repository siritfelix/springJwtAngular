package com.coderio.task.controller.dto;

import lombok.Builder;

@Builder
public record ResponseDto(String code, String message) {

}
