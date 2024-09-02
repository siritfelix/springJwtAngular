package com.coderio.task.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.coderio.task.controller.dto.ResponseDto;
import com.coderio.task.repository.TaskRepository;
import com.coderio.task.repository.UserRepository;
import com.coderio.task.repository.entity.TaskEntity;
import com.coderio.task.repository.entity.User;
import com.coderio.task.service.TaskService;
import com.coderio.task.service.domain.Task;
import com.coderio.task.shared.configuration.MenssageResponse;
import com.coderio.task.shared.exception.ConflictException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final MenssageResponse menssageResponse;

    @Override
    public List<Task> findAll(String username) {
        User user = this.findUser(username);
        List<TaskEntity> taskEntities = taskRepository.findByUser(user);
        return taskEntities.stream().map(TaskEntity::toTask).toList();
    }

    @Override
    public ResponseDto save(Task task, String username) {
        User user = this.findUser(username);
        TaskEntity taskEntity = new TaskEntity(task);
        taskEntity.setUser(user);
        user.addTask(taskEntity);
        userRepository.save(user);
        return menssageResponse.getResponseDtoByCode(MenssageResponse.OK);
    }

    @Override
    public ResponseDto upDate(Task task, String username) {
        log.info("Task:{}", task);
        User user = this.findUser(username);
        TaskEntity taskEntity = user.getTaskEntities().stream()
                .filter(taskEt -> taskEt.getId().equals(task.getId()))
                .findFirst().orElseThrow(() -> new ConflictException(ResponseDto.builder()
                        .code(MenssageResponse.E400)
                        .message(menssageResponse.getMessages().get(MenssageResponse.E400)
                                .concat(username))
                        .build()));
        this.upDateField(taskEntity, task);
        userRepository.save(user);
        return menssageResponse.getResponseDtoByCode(MenssageResponse.OK);

    }

    @Override
    public ResponseDto removeById(Integer id, String username) {
        log.info("Task:{}", id);
        User user = this.findUser(username);
        TaskEntity taskEntity = user.getTaskEntities().stream()
                .filter(taskEt -> taskEt.getId().equals(id))
                .findFirst().orElseThrow(() -> new ConflictException(ResponseDto.builder()
                        .code(MenssageResponse.E400)
                        .message(menssageResponse.getMessages().get(MenssageResponse.E400)
                                .concat(username))
                        .build()));
        user.getTaskEntities().remove(taskEntity);
        userRepository.save(user);
        return menssageResponse.getResponseDtoByCode(MenssageResponse.OK);
    }

    @Override
    public List<Task> findAllByFilter(Boolean completada, String prioridad, String username) {
        User user = this.findUser(username);

        return user.getTaskEntities().stream().filter(task -> (Objects.isNull(completada) ? true
                : completada.equals(task.getCompletada()))
                && (Objects.isNull(prioridad) ? true
                        : prioridad.equals(task.getPrioridad())))
                .map(TaskEntity::toTask).toList();
    }

    private User findUser(String username) {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new ConflictException(ResponseDto.builder()
                    .code(MenssageResponse.E409)
                    .message(menssageResponse.getMessages().get(MenssageResponse.E409)
                            .concat(username))
                    .build());
        }
    }

    private void upDateField(TaskEntity taskEntity, Task task) {
        taskEntity.setTitulo(task.getTitulo());
        taskEntity.setCompletada(task.getCompletada());
        taskEntity.setPrioridad(task.getPrioridad());
        taskEntity.setDescripcion(task.getDescripcion());
        taskEntity.setEtiquetas(task.getEtiquetas());
        taskEntity.setAsignadoA(task.getAsignadoA());

    }

}
