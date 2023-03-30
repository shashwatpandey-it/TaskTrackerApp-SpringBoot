package com.tasktrackerapplication.controllers;


import java.util.UUID;


import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import com.tasktrackerapplication.models.TaskModel;
import com.tasktrackerapplication.models.User;
import com.tasktrackerapplication.services.ReminderEmailService;
import com.tasktrackerapplication.services.TaskService;
import ch.qos.logback.core.util.Duration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/task-tracker/task-control")
public class TaskFormController {

	private final Logger logger = LoggerFactory.getLogger(TaskFormController.class);
	
	@Autowired
	private TaskService taskService;
	@Autowired
	private ReminderEmailService reminderEmailService;
	@Autowired
	private JobScheduler jobScheduler;
	
	@GetMapping("/add-task")
	public String taskForm(TaskModel taskModel) {
	
		logger.info("returned add task form page from taskform controller");
		return "taskform";
	}
	
	@PostMapping("/task")
	public String createTask(@Valid @ModelAttribute TaskModel taskModel , Model model, HttpServletRequest request) {
		
		TaskModel task = new TaskModel();
		task.setTaskDescription(taskModel.getTaskDescription());
		task.setStatus(false);
		task.setUser((User)request.getAttribute("user"));
		String jobId = jobScheduler.scheduleRecurrently(UUID.randomUUID().toString(), java.time.Duration.ofHours(4), () -> reminderEmailService.sendReminderMail(task.getUser().getEmail(), task.getTaskDescription()));
		logger.info("job scheduled");
		task.setJobId(jobId);
		
		taskService.save(task);
		logger.info("saved a new task to database");
		return "redirect:/task-tracker/index";
	}
	
	@GetMapping("/delete{id}")
	public String delete(@PathVariable("id") Integer id, Model model) {
		TaskModel taskModel = taskService.getById(id).orElseThrow(() -> new IllegalArgumentException("task not found"));
		jobScheduler.delete(taskModel.getJobId());
		logger.info("scheduled job deleted");
		taskService.delete(taskModel);
		
		logger.info("deleted a task from database");
		return "redirect:/task-tracker/index";
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
		if(oldTaskModel.isStatus()) {
			jobScheduler.delete(oldTaskModel.getJobId());
			logger.info("scheduled job deleted as the task was completed...");
		}
		taskService.save(oldTaskModel);
		
		return "redirect:/task-tracker/index";
	}
	
}
