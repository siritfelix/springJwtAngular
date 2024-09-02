package com.coderio.task.service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {
    private Integer id;
    private String titulo;
    private Boolean completada;
    private String prioridad;
    private String descripcion;
    private String etiquetas;
    private String asignadoA;
}
