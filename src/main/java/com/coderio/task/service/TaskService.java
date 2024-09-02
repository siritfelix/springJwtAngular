package com.coderio.task.service;

import java.util.List;

import com.coderio.task.controller.dto.ResponseDto;
import com.coderio.task.service.domain.Task;

public interface TaskService {

    List<Task> findAll(String username);

    ResponseDto save(Task task, String username);

    ResponseDto upDate(Task task, String username);

    ResponseDto removeById(Integer id, String username);

    List<Task> findAllByFilter(Boolean completada, String prioridad, String username);
}
