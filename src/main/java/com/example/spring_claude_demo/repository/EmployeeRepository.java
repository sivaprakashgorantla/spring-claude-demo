package com.example.spring_claude_demo.repository;

import com.example.spring_claude_demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // Custom query methods
    List<Employee> findByLastName(String lastName);
    List<Employee> findByPosition(String position);
    List<Employee> findByEmailContaining(String emailPart);
    List<Employee> findBySalaryGreaterThanEqual(Double minSalary);
}
