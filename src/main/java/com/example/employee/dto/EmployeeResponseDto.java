package com.example.employee.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.employee.entities.EmployeeStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {
	Long id;
	String firstName;
	String lastName;
	String email;
	String phone;
	String department;
	String designation;
	BigDecimal salary;
	EmployeeStatus status;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
}
