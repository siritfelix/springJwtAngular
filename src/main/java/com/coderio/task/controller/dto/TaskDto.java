package com.coderio.task.controller.dto;

import com.coderio.task.service.domain.Task;
import com.coderio.task.shared.configuration.MenssageResponse;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class TaskDto {
    private Integer id;
    @NotNull(message = MenssageResponse.E402)
    private String titulo;
    @NotNull(message = MenssageResponse.E404)
    private Boolean completada;
    @Pattern(regexp = "^(baja|alta|media)$", message = MenssageResponse.E405)
    private String prioridad;
    private String descripcion;
    private String etiquetas;
    private String asignadoA;

    public TaskDto(Task task) {
        this.id = task.getId();
        this.titulo = task.getTitulo();
        this.completada = task.getCompletada();
        this.prioridad = task.getPrioridad();
        this.descripcion = task.getDescripcion();
        this.etiquetas = task.getEtiquetas();
        this.asignadoA = task.getAsignadoA();
    }

    public Task toTask() {
        return Task.builder().id(id).titulo(titulo).completada(completada).prioridad(prioridad)
                .descripcion(descripcion).etiquetas(etiquetas).asignadoA(asignadoA)
                .build();
    }

}
