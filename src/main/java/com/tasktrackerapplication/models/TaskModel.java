package com.tasktrackerapplication.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jobrunr.jobs.JobId;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	private boolean status;
	
	private LocalDateTime createdDateTime;
	
	private LocalDateTime updatedDateTime;
	
	private String jobId;
	
	//private LocalDateTime deadlineDateTime;
	@ManyToOne(fetch = FetchType.LAZY,optional = false)
	@JoinColumn(name="user_id", nullable = false)
	private User user;

	@Override
	public String toString() {
		return "TaskModel [id=" + id + ", taskDescription=" + taskDescription + ", status=" + status
				+ ", createdDateTime=" + createdDateTime + ", updatedDateTime=" + updatedDateTime + "]";
	}

	
	
	
}
