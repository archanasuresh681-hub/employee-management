package com.example.employee.service;

import java.util.Locale;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.employee.dto.EmployeeRequestDto;
import com.example.employee.dto.EmployeeResponseDto;
import com.example.employee.entities.Employee;
import com.example.employee.entities.EmployeeStatus;
import com.example.employee.exception.DuplicateResourceException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.repository.IEmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements IEmployeeService {

	private final IEmployeeRepository employeeRepository;

	@Override
	public EmployeeResponseDto createEmployee(EmployeeRequestDto request) {
		String email = normalizeEmail(request.getEmail());

		if (employeeRepository.existsByEmailIgnoreCase(email)) {
			throw new DuplicateResourceException("An employee with this email already exists");
		}

		if (request.getPhone() != null && !request.getPhone().isBlank()
				&& employeeRepository.existsByPhone(request.getPhone().trim())) {
			throw new DuplicateResourceException("Mobile number already exists: " + request.getPhone());
		}

		Employee employee = new Employee();

		employee.setFirstName(request.getFirstName().trim());
		employee.setLastName(request.getLastName().trim());
		employee.setEmail(email);
		employee.setPhone(request.getPhone());
		employee.setDepartment(request.getDepartment());
		employee.setDesignation(request.getDesignation());
		employee.setSalary(request.getSalary());

		employee.setStatus(request.getStatus() != null ? request.getStatus() : EmployeeStatus.ACTIVE);

		Employee savedEmployee = employeeRepository.save(employee);

		return mapToResponse(savedEmployee);
	}

	@Override
	public Page<EmployeeResponseDto> getAllEmployees(String search, EmployeeStatus status, int page, int size,
			String sortBy, String direction) {

		if (page < 0) {
			throw new IllegalArgumentException("Page number cannot be negative");
		}

		if (size < 1 || size > 100) {
			throw new IllegalArgumentException("Page size must be between 1 and 100");
		}

		// sorting by known employee field
		Set<String> allowedSortFields = Set.of("id", "firstName", "lastName", "email", "department", "designation",
				"salary", "status", "createdAt", "updatedAt");

		if (!allowedSortFields.contains(sortBy)) {
			throw new IllegalArgumentException("Invalid sort field: " + sortBy);
		}

		Sort.Direction sortDirection;

		try {
			sortDirection = Sort.Direction.fromString(direction);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Sort direction must be ASC or DESC");
		}

		String normalizedSearch = (search == null || search.isBlank()) ? null : search.trim();

		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

		return employeeRepository.searchEmployees(normalizedSearch, status, pageable).map(this::mapToResponse);
	}

	@Override
	public EmployeeResponseDto getEmployeeById(Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

		return mapToResponse(employee);
	}

	@Override
	public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto request) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

		String email = normalizeEmail(request.getEmail());

		if (employeeRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
			throw new DuplicateResourceException("Another employee already uses this email");
		}

		if (request.getPhone() != null && !request.getPhone().isBlank()
				&& employeeRepository.existsByPhoneAndIdNot(request.getPhone().trim(), id)) {
			throw new DuplicateResourceException("Mobile number already exists: " + request.getPhone());
		}

		employee.setFirstName(request.getFirstName().trim());
		employee.setLastName(request.getLastName().trim());
		employee.setEmail(email);
		employee.setPhone(request.getPhone());
		employee.setDepartment(request.getDepartment());
		employee.setDesignation(request.getDesignation());
		employee.setSalary(request.getSalary());

		// Preserve existing status if the request does not specify one.
		if (request.getStatus() != null) {
			employee.setStatus(request.getStatus());
		}

		Employee updatedEmployee = employeeRepository.save(employee);

		return mapToResponse(updatedEmployee);
	}

	@Override
	public void deleteEmployee(Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

		employeeRepository.delete(employee);

	}

	private String normalizeEmail(String email) {
		return email.trim().toLowerCase(Locale.ROOT);
	}

	// MAP ENTITY TO RESPONSE DTO
	private EmployeeResponseDto mapToResponse(Employee employee) {

		return new EmployeeResponseDto(employee.getId(), employee.getFirstName(), employee.getLastName(),
				employee.getEmail(), employee.getPhone(), employee.getDepartment(), employee.getDesignation(),
				employee.getSalary(), employee.getStatus(), employee.getCreatedAt(), employee.getUpdatedAt());
	}
}
