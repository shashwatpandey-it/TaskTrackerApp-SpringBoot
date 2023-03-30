package com.tasktrackerapplication.requestandresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

	private boolean tokenGenerated;
	private String token;
	private Integer userId;
}
