package com.tasktrackerapplication.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tasktrackerapplication.models.User;
import com.tasktrackerapplication.services.TaskService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/task-tracker")
public class HomeController {

	private final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private TaskService taskService;
	
	@GetMapping
	public ModelAndView entryPage() {
		ModelAndView modelAndView = new ModelAndView("entry");
		logger.info("returned entry page from home controller");
		return modelAndView;
	}
	
	@GetMapping("/index")
	public ModelAndView home(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("taskItems", taskService.getAllTasks((User)request.getAttribute("user")));
		modelAndView.addObject("user", request.getAttribute("user"));
		logger.info("returned index page from home controller");
		//logger.info(taskService.getAllTasks().toString());
		return modelAndView;
	}
	
}
