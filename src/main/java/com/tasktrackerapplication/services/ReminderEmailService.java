package com.tasktrackerapplication.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ReminderEmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String sender;
	private Logger logger = LoggerFactory.getLogger(ReminderEmailService.class);
	
	public void sendReminderMail(String recipient, String taskDescription) {
		try {
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setFrom(sender);
			simpleMailMessage.setTo(recipient);
			simpleMailMessage.setSubject("Reminder mail for uncompleted task.");
			simpleMailMessage.setText("The task named: "+ taskDescription +" is not yet marked done. Please make sure you complete your task on time and mark it as done.");
			
			javaMailSender.send(simpleMailMessage);
			logger.info("Mail sent successfully...");
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("Something went wrong with mailing service...mail not sent!!");
		}
	}
}
