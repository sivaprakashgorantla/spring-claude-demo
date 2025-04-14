package com.example.spring_claude_demo.service;

import com.example.spring_claude_demo.exception.ResourceNotFoundException;
import com.example.spring_claude_demo.model.Employee;
import com.example.spring_claude_demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Create
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Read - All employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Read - Single employee by ID
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    // Read - Employees by last name
    public List<Employee> getEmployeesByLastName(String lastName) {
        return employeeRepository.findByLastName(lastName);
    }

    // Read - Employees by position
    public List<Employee> getEmployeesByPosition(String position) {
        return employeeRepository.findByPosition(position);
    }

    // Read - Employees by email containing
    public List<Employee> getEmployeesByEmailContaining(String emailPart) {
        return employeeRepository.findByEmailContaining(emailPart);
    }

    // Read - Employees by minimum salary
    public List<Employee> getEmployeesByMinimumSalary(Double minSalary) {
        return employeeRepository.findBySalaryGreaterThanEqual(minSalary);
    }

    // Update
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPhoneNumber(employeeDetails.getPhoneNumber());
        employee.setPosition(employeeDetails.getPosition());
        employee.setSalary(employeeDetails.getSalary());
        employee.setHireDate(employeeDetails.getHireDate());
        
        return employeeRepository.save(employee);
    }

    // Delete
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        
        employeeRepository.delete(employee);
    }

    // Check if employee exists
    public boolean employeeExists(Long id) {
        return employeeRepository.existsById(id);
    }
}
