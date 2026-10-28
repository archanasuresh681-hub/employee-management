package com.example.employee.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.dto.ApiResponseDto;
import com.example.employee.dto.EmployeeRequestDto;
import com.example.employee.dto.EmployeeResponseDto;
import com.example.employee.entities.EmployeeStatus;
import com.example.employee.service.IEmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
	private final IEmployeeService service;

	// Create employees
	@PostMapping
	public ResponseEntity<ApiResponseDto<EmployeeResponseDto>> createEmployee(
			@Valid @RequestBody EmployeeRequestDto request) {

		EmployeeResponseDto response = service.createEmployee(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponseDto<>(true, "Employee created successfully", response));
	}

	// view all employees
	@GetMapping
	public ResponseEntity<Page<EmployeeResponseDto>> getAllEmployees(@RequestParam(required = false) String search,
			@RequestParam(required = false) EmployeeStatus status, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "ASC") String direction) {

		Page<EmployeeResponseDto> employees = service.getAllEmployees(search, status, page, size, sortBy, direction);

		return ResponseEntity.ok(employees);
	}

	// get employees by id
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id) {

		EmployeeResponseDto response = service.getEmployeeById(id);

		return ResponseEntity.ok(response);
	}

	// update employee
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponseDto<EmployeeResponseDto>> updateEmployee(@PathVariable Long id,
			@Valid @RequestBody EmployeeRequestDto request) {

		EmployeeResponseDto response = service.updateEmployee(id, request);

		return ResponseEntity.ok(new ApiResponseDto<>(true, "Employee updated successfully", response));
	}

	// delete employee
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponseDto<Object>> deleteEmployee(@PathVariable Long id) {

		service.deleteEmployee(id);

		return ResponseEntity.ok(new ApiResponseDto<>(true, "Employee deleted successfully", null));
	}
}
