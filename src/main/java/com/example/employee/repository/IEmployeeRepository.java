package com.example.employee.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.employee.entities.Employee;
import com.example.employee.entities.EmployeeStatus;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {

	// Find an employee by email, ignoring case
	Optional<Employee> findByEmailIgnoreCase(String email);

	// Check whether an email already exists
	boolean existsByEmailIgnoreCase(String email);

	// Check whether another employee uses the email
	boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

	@Query("""
			SELECT e FROM Employee e
			WHERE (
			    CAST(:search AS string) IS NULL
			    OR LOWER(e.firstName) LIKE
			        LOWER(CONCAT('%', CAST(:search AS string), '%'))
			    OR LOWER(e.lastName) LIKE
			        LOWER(CONCAT('%', CAST(:search AS string), '%'))
			    OR LOWER(e.email) LIKE
			        LOWER(CONCAT('%', CAST(:search AS string), '%'))
			    OR LOWER(e.department) LIKE
			        LOWER(CONCAT('%', CAST(:search AS string), '%'))
			    OR LOWER(e.designation) LIKE
			        LOWER(CONCAT('%', CAST(:search AS string), '%'))
			)
			AND (
			    CAST(:status AS string) IS NULL
			    OR e.status = :status
			)
			""")
	Page<Employee> searchEmployees(@Param("search") String search, @Param("status") EmployeeStatus status,
			Pageable pageable);

	// check phone number exist
	boolean existsByPhone(String phone);

	boolean existsByPhoneAndIdNot(String phone, Long id);
}
