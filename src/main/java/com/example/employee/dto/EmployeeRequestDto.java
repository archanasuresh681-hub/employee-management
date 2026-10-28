package com.example.employee.dto;

import java.math.BigDecimal;

import com.example.employee.entities.EmployeeStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDto {
	@NotBlank(message = "First name is required")
	@Size(max = 100, message = "First name cannot exceed 100 characters")
	String firstName;

	@NotBlank(message = "Last name is required")
	@Size(max = 100, message = "Last name cannot exceed 100 characters")
	String lastName;

	@NotBlank(message = "Email is required")
	@Email(message = "Please provide a valid email address")
	@Size(max = 150, message = "Email cannot exceed 150 characters")
	String email;

	@NotBlank(message = "Mobile number is required")
	@Pattern(regexp = "^[0-9]{10}$",message = "Mobile number must contain exactly 10 digits"
)
	String phone;

	@Size(max = 100)
	String department;

	@Size(max = 100)
	String designation;

	@NotNull(message = "Salary is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than zero")
	@Digits(integer = 10, fraction = 2, message = "Salary must have at most 10 integer digits and 2 decimal places")
	BigDecimal salary;

	EmployeeStatus status;
}
