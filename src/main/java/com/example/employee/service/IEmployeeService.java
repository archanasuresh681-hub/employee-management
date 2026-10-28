package com.example.employee.service;

import org.springframework.data.domain.Page;

import com.example.employee.dto.EmployeeRequestDto;
import com.example.employee.dto.EmployeeResponseDto;
import com.example.employee.entities.EmployeeStatus;

public interface IEmployeeService {
	EmployeeResponseDto createEmployee(EmployeeRequestDto request);

	Page<EmployeeResponseDto> getAllEmployees(String search, EmployeeStatus status, int page, int size, String sortBy,
			String direction);

	EmployeeResponseDto getEmployeeById(Long id);

	EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto request);

	void deleteEmployee(Long id);

}
