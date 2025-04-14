package com.example.spring_claude_demo.controller;

import com.example.spring_claude_demo.exception.ResourceNotFoundException;
import com.example.spring_claude_demo.model.Employee;
import com.example.spring_claude_demo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee employee1;
    private Employee employee2;
    private List<Employee> employeeList;

    @BeforeEach
    void setUp() {
        // Create test data
        employee1 = new Employee(
                "John", 
                "Doe", 
                "john.doe@example.com", 
                "555-1234", 
                "Developer", 
                75000.0, 
                LocalDate.of(2020, 1, 15)
        );
        employee1.setId(1L);

        employee2 = new Employee(
                "Jane", 
                "Doe", 
                "jane.doe@example.com", 
                "555-5678", 
                "Manager", 
                95000.0, 
                LocalDate.of(2019, 5, 10)
        );
        employee2.setId(2L);

        employeeList = Arrays.asList(employee1, employee2);
    }

    @Test
    void createEmployee_ShouldReturnCreatedEmployee() {
        // Arrange
        Employee newEmployee = new Employee(
                "New", 
                "Employee", 
                "new.employee@example.com", 
                "555-9876", 
                "Analyst", 
                65000.0, 
                LocalDate.now()
        );
        
        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(newEmployee);

        // Act
        ResponseEntity<Employee> response = employeeController.createEmployee(newEmployee);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newEmployee, response.getBody());
        verify(employeeService, times(1)).saveEmployee(any(Employee.class));
    }

    @Test
    void getAllEmployees_ShouldReturnAllEmployees() {
        // Arrange
        when(employeeService.getAllEmployees()).thenReturn(employeeList);

        // Act
        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(employeeList, response.getBody());
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void getEmployeeById_WhenEmployeeExists_ShouldReturnEmployee() {
        // Arrange
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee1));

        // Act
        ResponseEntity<Employee> response = employeeController.getEmployeeById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee1, response.getBody());
        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void getEmployeeById_WhenEmployeeDoesNotExist_ShouldThrowException() {
        // Arrange
        Long nonExistentId = 999L;
        when(employeeService.getEmployeeById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeController.getEmployeeById(nonExistentId);
        });
        verify(employeeService, times(1)).getEmployeeById(nonExistentId);
    }

    @Test
    void getEmployeesByLastName_WhenEmployeesExist_ShouldReturnEmployees() {
        // Arrange
        String lastName = "Doe";
        when(employeeService.getEmployeesByLastName(lastName)).thenReturn(employeeList);

        // Act
        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByLastName(lastName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeList, response.getBody());
        verify(employeeService, times(1)).getEmployeesByLastName(lastName);
    }

    @Test
    void getEmployeesByLastName_WhenNoEmployeesExist_ShouldReturnNoContent() {
        // Arrange
        String lastName = "NonExistent";
        when(employeeService.getEmployeesByLastName(lastName)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByLastName(lastName);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(employeeService, times(1)).getEmployeesByLastName(lastName);
    }

    @Test
    void getEmployeesByPosition_WhenEmployeesExist_ShouldReturnEmployees() {
        // Arrange
        String position = "Developer";
        List<Employee> developers = Collections.singletonList(employee1);
        when(employeeService.getEmployeesByPosition(position)).thenReturn(developers);

        // Act
        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByPosition(position);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(developers, response.getBody());
        verify(employeeService, times(1)).getEmployeesByPosition(position);
    }

    @Test
    void getEmployeesByPosition_WhenNoEmployeesExist_ShouldReturnNoContent() {
        // Arrange
        String position = "NonExistent";
        when(employeeService.getEmployeesByPosition(position)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByPosition(position);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(employeeService, times(1)).getEmployeesByPosition(position);
    }

    @Test
    void getEmployeesByEmailContaining_WhenEmployeesExist_ShouldReturnEmployees() {
        // Arrange
        String emailPart = "example.com";
        when(employeeService.getEmployeesByEmailContaining(emailPart)).thenReturn(employeeList);

        // Act
        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByEmailContaining(emailPart);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeList, response.getBody());
        verify(employeeService, times(1)).getEmployeesByEmailContaining(emailPart);
    }

    @Test
    void getEmployeesByEmailContaining_WhenNoEmployeesExist_ShouldReturnNoContent() {
        // Arrange
        String emailPart = "nonexistent";
        when(employeeService.getEmployeesByEmailContaining(emailPart)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByEmailContaining(emailPart);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(employeeService, times(1)).getEmployeesByEmailContaining(emailPart);
    }

    @Test
    void getEmployeesByMinimumSalary_WhenEmployeesExist_ShouldReturnEmployees() {
        // Arrange
        Double minSalary = 80000.0;
        List<Employee> highPaidEmployees = Collections.singletonList(employee2);
        when(employeeService.getEmployeesByMinimumSalary(minSalary)).thenReturn(highPaidEmployees);

        // Act
        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByMinimumSalary(minSalary);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(highPaidEmployees, response.getBody());
        verify(employeeService, times(1)).getEmployeesByMinimumSalary(minSalary);
    }

    @Test
    void getEmployeesByMinimumSalary_WhenNoEmployeesExist_ShouldReturnNoContent() {
        // Arrange
        Double minSalary = 100000.0;
        when(employeeService.getEmployeesByMinimumSalary(minSalary)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByMinimumSalary(minSalary);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(employeeService, times(1)).getEmployeesByMinimumSalary(minSalary);
    }

    @Test
    void updateEmployee_WhenEmployeeExists_ShouldReturnUpdatedEmployee() {
        // Arrange
        Long id = 1L;
        Employee updatedEmployee = new Employee(
                "Updated", 
                "Employee", 
                "updated.employee@example.com", 
                "555-0000", 
                "Senior Developer", 
                85000.0, 
                LocalDate.now()
        );
        
        when(employeeService.updateEmployee(eq(id), any(Employee.class))).thenReturn(updatedEmployee);

        // Act
        ResponseEntity<Employee> response = employeeController.updateEmployee(id, updatedEmployee);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedEmployee, response.getBody());
        verify(employeeService, times(1)).updateEmployee(eq(id), any(Employee.class));
    }

    @Test
    void deleteEmployee_ShouldReturnSuccessResponse() {
        // Arrange
        Long id = 1L;
        doNothing().when(employeeService).deleteEmployee(id);

        // Act
        ResponseEntity<Map<String, Boolean>> response = employeeController.deleteEmployee(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().get("deleted"));
        verify(employeeService, times(1)).deleteEmployee(id);
    }
}
