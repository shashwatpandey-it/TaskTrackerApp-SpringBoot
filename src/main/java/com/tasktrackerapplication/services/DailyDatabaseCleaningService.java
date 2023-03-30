package com.tasktrackerapplication.services;

import java.util.ArrayList;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasktrackerapplication.models.TaskModel;

import jakarta.annotation.PostConstruct;


@Service
public class DailyDatabaseCleaningService {
	
	@Autowired
	private TaskService taskService;
	@Autowired
	private JobScheduler jobScheduler;
	
	public void performDailyDatabaseCleaning() {
		Iterable<TaskModel> tasksInDatabase = new ArrayList<>();
		tasksInDatabase = taskService.getAllTasks();
		for(TaskModel task : tasksInDatabase) {
			if(task.getJobId() != null) {
				jobScheduler.delete(task.getJobId());
			}
		}
		taskService.deleteAllTasks();
		//jobScheduler.scheduleRecurrently("Daily-Cleaning-Tasks", Cron.daily(), () -> taskService.deleteAllTasks());
	}
	
	@PostConstruct
	public void scheduleExecutionOfDailyMaintenance() {
		jobScheduler.scheduleRecurrently("Daily-Maintenance", Cron.daily(), () -> performDailyDatabaseCleaning());
	}
	
	
}
