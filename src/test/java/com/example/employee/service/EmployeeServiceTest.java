package com.example.employee.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.employee.service.EmployeeServiceImpl;
import com.example.employee.repository.IEmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
	@Mock
	private IEmployeeRepository employeeRepository;

	@InjectMocks
	private EmployeeServiceImpl employeeService;

	@Test
	void getAllEmployees_shouldRejectNegativePage() {

		assertThrows(IllegalArgumentException.class,
				() -> employeeService.getAllEmployees(null, null, -1, 10, "id", "ASC"));

		verifyNoInteractions(employeeRepository);
	}

	@Test
	void getAllEmployees_shouldRejectInvalidPageSize() {

		assertThrows(IllegalArgumentException.class,
				() -> employeeService.getAllEmployees(null, null, 0, 0, "id", "ASC"));

		verifyNoInteractions(employeeRepository);
	}

	@Test
	void getAllEmployees_shouldRejectInvalidSortField() {

		assertThrows(IllegalArgumentException.class,
				() -> employeeService.getAllEmployees(null, null, 0, 10, "invalidField", "ASC"));

		verifyNoInteractions(employeeRepository);
	}

	@Test
	void getAllEmployees_shouldRejectInvalidSortDirection() {

		assertThrows(IllegalArgumentException.class,
				() -> employeeService.getAllEmployees(null, null, 0, 10, "id", "INVALID"));

		verifyNoInteractions(employeeRepository);
	}
}
