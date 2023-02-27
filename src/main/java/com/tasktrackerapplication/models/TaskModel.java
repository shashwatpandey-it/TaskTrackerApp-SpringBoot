package com.tasktrackerapplication.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name= "task_table")
public class TaskModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String taskDescription;
	
	private boolean isComplete;
	
	private LocalDateTime createdDateTime;
	
	private LocalDateTime updatedDateTime;

	@Override
	public String toString() {
		return "TaskModel [id=" + id + ", taskDescription=" + taskDescription + ", isComplete=" + isComplete
				+ ", createdDateTime=" + createdDateTime + ", updatedDateTime=" + updatedDateTime + "]";
	}
	
	
}
