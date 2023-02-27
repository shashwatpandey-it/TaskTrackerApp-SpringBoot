package com.tasktrackerapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasktrackerapplication.models.TaskModel;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Integer>{

}
