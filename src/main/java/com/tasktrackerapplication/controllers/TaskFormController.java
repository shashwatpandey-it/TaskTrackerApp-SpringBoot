package com.tasktrackerapplication.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tasktrackerapplication.models.TaskModel;
import com.tasktrackerapplication.services.TaskService;
import jakarta.validation.Valid;

@Controller
public class TaskFormController {

	private final Logger logger = LoggerFactory.getLogger(TaskFormController.class);
	
	@Autowired
	private TaskService taskService;
	
	@GetMapping("add-task")
	public String taskForm(TaskModel taskModel) {
	
		logger.info("returned add task form page from taskform controller");
		return "taskform";
	}
	
	@PostMapping("/task")
	public String createTask(@Valid @ModelAttribute TaskModel taskModel , Model model) {
		
		TaskModel task = new TaskModel();
		task.setTaskDescription(taskModel.getTaskDescription());
		task.setStatus(false);
		
		taskService.save(task);
		logger.info("saved a new task to database");
		return "redirect:/index";
	}
	
	@GetMapping("/delete{id}")
	public String delete(@PathVariable("id") Integer id, Model model) {
		TaskModel taskModel = taskService.getById(id).orElseThrow(() -> new IllegalArgumentException("task not found"));
		taskService.delete(taskModel);
		
		logger.info("deleted a task from database");
		return "redirect:/index";
	}
	
	@GetMapping("/edit{id}")
	public String editTaskFotm(@PathVariable("id") Integer id, Model model) {
		TaskModel taskModel = taskService.getById(id).orElseThrow(() -> new IllegalArgumentException("task not found"));
		model.addAttribute("task", taskModel);
		
		return "edittaskform";
	}
	
	@PostMapping("/task/{id}")
	public String editTask(@PathVariable("id") Integer id, @Valid @ModelAttribute TaskModel newTaskModel, Model model) {
		TaskModel oldTaskModel = taskService.getById(id).orElseThrow(() -> new IllegalArgumentException("task not found"));
		
		oldTaskModel.setTaskDescription(newTaskModel.getTaskDescription());
		oldTaskModel.setStatus(newTaskModel.isStatus());
		taskService.save(oldTaskModel);
		
		return "redirect:/index";
	}
	
}
