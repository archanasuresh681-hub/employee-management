package com.example.employee.exception;

import java.time.Instant;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorResponse {
	Instant timestamp;
	int status;
	String error;
	String message;
	String path;
	Map<String, String> validationErrors;
}
