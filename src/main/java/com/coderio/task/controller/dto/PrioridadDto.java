package com.coderio.task.controller.dto;

import lombok.Getter;

@Getter
public enum PrioridadDto {
    ALTA("alta"), MEDIA("media"), BAJA("baja");

    private String valor;

    private PrioridadDto(String valor) {
        this.valor = valor;
    }

}
