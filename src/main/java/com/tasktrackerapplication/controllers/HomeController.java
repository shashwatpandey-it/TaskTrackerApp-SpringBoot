package com.tasktrackerapplication.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.tasktrackerapplication.models.TaskModel;
import com.tasktrackerapplication.services.TaskService;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import ch.qos.logback.core.model.Model;

@Controller
public class HomeController {

	private final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private TaskService taskService;
	
	@GetMapping("/index")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("taskItems", taskService.getAllTasks());
		
		logger.info("returned index page from home controller");
		logger.info(taskService.getAllTasks().toString());
		return modelAndView;
	}
	
}
