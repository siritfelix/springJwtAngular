package com.coderio.task.repository.entity;

import com.coderio.task.service.domain.Task;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titulo;
    private Boolean completada;
    private String prioridad;
    private String descripcion;
    private String etiquetas;
    private String asignadoA;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

    public Task toTask() {
        return Task.builder()
                .id(id)
                .titulo(titulo).completada(completada).prioridad(prioridad).descripcion(descripcion)
                .etiquetas(etiquetas).asignadoA(asignadoA)
                .build();
    }

    public TaskEntity(Task task) {
        this.titulo = task.getTitulo();
        this.completada = task.getCompletada();
        this.prioridad = task.getPrioridad();
        this.descripcion = task.getDescripcion();
        this.etiquetas = task.getEtiquetas();
        this.asignadoA = task.getAsignadoA();
    }
}
