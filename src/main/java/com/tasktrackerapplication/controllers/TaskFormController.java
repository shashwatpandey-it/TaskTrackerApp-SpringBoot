package com.tasktrackerapplication.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TaskFormController {

	private final Logger logger = LoggerFactory.getLogger(TaskFormController.class);
	
	@GetMapping("add-task")
	public String taskForm() {
	
		logger.info("returned add task form page from taskform controller");
		return "taskform";
	}
}
