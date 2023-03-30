package com.tasktrackerapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasktrackerapplication.models.TaskModel;
import com.tasktrackerapplication.models.User;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Integer>{

	Iterable<TaskModel> findByUser(User user);
}
