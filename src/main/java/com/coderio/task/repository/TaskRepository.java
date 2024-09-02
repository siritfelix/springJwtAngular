package com.coderio.task.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.coderio.task.repository.entity.TaskEntity;
import com.coderio.task.repository.entity.User;

public interface TaskRepository extends CrudRepository<TaskEntity, Integer> {
    List<TaskEntity> findByUser(User user);

}
