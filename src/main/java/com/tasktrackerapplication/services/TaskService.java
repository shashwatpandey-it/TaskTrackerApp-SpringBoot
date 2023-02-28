package com.tasktrackerapplication.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasktrackerapplication.models.TaskModel;
import com.tasktrackerapplication.repositories.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;
	
	// saving a task
	public TaskModel save(TaskModel taskModel) {
		if(taskModel.getId() == null) {
			taskModel.setCreatedDateTime(LocalDateTime.now());
		}
		taskModel.setUpdatedDateTime(LocalDateTime.now());
		return taskRepository.save(taskModel);
	}
	
	// deleting a task
	public void delete(TaskModel taskModel) {
		taskRepository.delete(taskModel);
	}
	
	// getting a task by id
	public Optional<TaskModel> getById(Integer id){
		Optional<TaskModel> taskmodelOptional = taskRepository.findById(id);
		return taskmodelOptional;
	}
	
	// getting all tasks as list
	public Iterable<TaskModel> getAllTasks(){
		return taskRepository.findAll();
	}
}
