package com.coderio.task.shared.configuration;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.coderio.task.controller.dto.ResponseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "ms")
public class MenssageResponse {
    private Map<String, String> messages;
    public static final String SP = ";";
    public static final String OK = "OK";
    public static final String OK_RT = "OK_RT";
    public static final String OK_LOGOUT = "OK_LOGOUT";
    public static final String S001 = "S001";
    public static final String S002 = "S002";
    public static final String S003 = "S003";
    public static final String BR400 = "BR400";
    public static final String F401 = "F401";
    public static final String E500 = "E500";
    public static final String E403 = "E403";
    public static final String E409 = "E409";
    public static final String E400 = "E400";
    public static final String E401 = "E401";
    public static final String E402 = "E402";
    public static final String E404 = "E404";
    public static final String E405 = "E405";

    public ResponseDto getResponseDtoByCode(String code) {
        return ResponseDto.builder().code(code).message(messages.get(code))
                .build();
    }

}
