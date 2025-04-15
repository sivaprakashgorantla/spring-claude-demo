package com.example.spring_claude_demo.controller;

import com.example.spring_claude_demo.model.Employee;
import com.example.spring_claude_demo.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    private ObjectMapper objectMapper;
    
    private List<Employee> employeeList;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
        
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        // Initialize test data
        employeeList = new ArrayList<>();
        
        Employee employee1 = new Employee(
                "John", 
                "Doe", 
                "john.doe@example.com", 
                "555-1234", 
                "Developer", 
                75000.0, 
                LocalDate.of(2020, 1, 15)
        );
        
        Employee employee2 = new Employee(
                "Jane", 
                "Doe", 
                "jane.doe@example.com", 
                "555-5678", 
                "Manager", 
                95000.0, 
                LocalDate.of(2019, 5, 10)
        );
        
        Employee employee3 = new Employee(
                "Bob", 
                "Smith", 
                "bob.smith@example.com", 
                "555-8765", 
                "Developer", 
                70000.0, 
                LocalDate.of(2021, 3, 20)
        );
        
        // Save employees to the database
        employeeList.add(employeeRepository.save(employee1));
        employeeList.add(employeeRepository.save(employee2));
        employeeList.add(employeeRepository.save(employee3));
    }
    
    @AfterEach
    void cleanup() {
        employeeRepository.deleteAll();
    }

    @Test
    void createEmployee_ShouldReturnCreatedEmployee() throws Exception {
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
        
        // Act
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee)));
        
        // Assert
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(newEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(newEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(newEmployee.getEmail())))
                .andExpect(jsonPath("$.position", is(newEmployee.getPosition())))
                .andExpect(jsonPath("$.salary", is(newEmployee.getSalary())));
    }

    @Test
    void getAllEmployees_ShouldReturnAllEmployees() throws Exception {
        // Act
        ResultActions response = mockMvc.perform(get("/api/employees"));
        
        // Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(employeeList.size())))
                .andExpect(jsonPath("$[0].firstName", is(employeeList.get(0).getFirstName())))
                .andExpect(jsonPath("$[1].firstName", is(employeeList.get(1).getFirstName())))
                .andExpect(jsonPath("$[2].firstName", is(employeeList.get(2).getFirstName())));
    }

    @Test
    void getEmployeeById_WhenEmployeeExists_ShouldReturnEmployee() throws Exception {
        // Arrange
        Long employeeId = employeeList.get(0).getId();
        
        // Act
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        
        // Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(employeeId.intValue())))
                .andExpect(jsonPath("$.firstName", is(employeeList.get(0).getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employeeList.get(0).getLastName())));
    }

    @Test
    void getEmployeeById_WhenEmployeeDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        Long nonExistentId = 999L;
        
        // Act
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", nonExistentId));
        
        // Assert
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getEmployeesByLastName_WhenEmployeesExist_ShouldReturnEmployees() throws Exception {
        // Arrange
        String lastName = "Doe";
        
        // Act
        ResultActions response = mockMvc.perform(get("/api/employees/lastName/{lastName}", lastName));
        
        // Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].lastName", is(lastName)))
                .andExpect(jsonPath("$[1].lastName", is(lastName)));
    }

    @Test
    void getEmployeesByLastName_WhenNoEmployeesExist_ShouldReturnNoContent() throws Exception {
        // Arrange
        String nonExistentLastName = "NonExistent";
        
        // Act
        ResultActions response = mockMvc.perform(get("/api/employees/lastName/{lastName}", nonExistentLastName));
        
        // Assert
        response.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void getEmployeesByPosition_WhenEmployeesExist_ShouldReturnEmployees() throws Exception {
        // Arrange
        String position = "Developer";
        
        // Act
        ResultActions response = mockMvc.perform(get("/api/employees/position/{position}", position));
        
        // Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].position", is(position)))
                .andExpect(jsonPath("$[1].position", is(position)));
    }

    @Test
    void getEmployeesByPosition_WhenNoEmployeesExist_ShouldReturnNoContent() throws Exception {
        // Arrange
        String nonExistentPosition = "NonExistent";
        
        // Act
        ResultActions response = mockMvc.perform(get("/api/employees/position/{position}", nonExistentPosition));
        
        // Assert
        response.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void getEmployeesByEmailContaining_WhenEmployeesExist_ShouldReturnEmployees() throws Exception {
        // Arrange
        String emailPart = "example.com";
        
        // Act
        ResultActions response = mockMvc.perform(get("/api/employees/email")
                .param("contains", emailPart));
        
        // Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].email", is(employeeList.get(0).getEmail())))
                .andExpect(jsonPath("$[1].email", is(employeeList.get(1).getEmail())))
                .andExpect(jsonPath("$[2].email", is(employeeList.get(2).getEmail())));
    }

    @Test
    void getEmployeesByEmailContaining_WhenNoEmployeesExist_ShouldReturnNoContent() throws Exception {
        // Arrange
        String nonExistentEmailPart = "nonexistent";
        
        // Act
        ResultActions response = mockMvc.perform(get("/api/employees/email")
                .param("contains", nonExistentEmailPart));
        
        // Assert
        response.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void getEmployeesByMinimumSalary_WhenEmployeesExist_ShouldReturnEmployees() throws Exception {
        // Arrange
        Double minSalary = 80000.0;
        
        // Act
        ResultActions response = mockMvc.perform(get("/api/employees/salary")
                .param("minSalary", minSalary.toString()));
        
        // Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].salary").value(95000.0));
    }

    @Test
    void getEmployeesByMinimumSalary_WhenNoEmployeesExist_ShouldReturnNoContent() throws Exception {
        // Arrange
        Double highMinSalary = 100000.0;
        
        // Act
        ResultActions response = mockMvc.perform(get("/api/employees/salary")
                .param("minSalary", highMinSalary.toString()));
        
        // Assert
        response.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void updateEmployee_WhenEmployeeExists_ShouldReturnUpdatedEmployee() throws Exception {
        // Arrange
        Long employeeId = employeeList.get(0).getId();
        Employee updatedEmployee = new Employee(
                "Updated", 
                "Employee", 
                "updated.employee@example.com", 
                "555-0000", 
                "Senior Developer", 
                85000.0, 
                LocalDate.now()
        );
        
        // Act
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        
        // Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(employeeId.intValue())))
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())))
                .andExpect(jsonPath("$.position", is(updatedEmployee.getPosition())))
                .andExpect(jsonPath("$.salary", is(updatedEmployee.getSalary())));
    }

    @Test
    void updateEmployee_WhenEmployeeDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        Long nonExistentId = 999L;
        Employee updatedEmployee = new Employee(
                "Updated", 
                "Employee", 
                "updated.employee@example.com", 
                "555-0000", 
                "Senior Developer", 
                85000.0, 
                LocalDate.now()
        );
        
        // Act
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        
        // Assert
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEmployee_WhenEmployeeExists_ShouldReturnSuccessResponse() throws Exception {
        // Arrange
        Long employeeId = employeeList.get(0).getId();
        
        // Act
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));
        
        // Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted", is(true)));
        
        // Verify employee is deleted
        mockMvc.perform(get("/api/employees/{id}", employeeId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEmployee_WhenEmployeeDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        Long nonExistentId = 999L;
        
        // Act
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", nonExistentId));
        
        // Assert
        response.andDo(print())
                .andExpect(status().isNotFound());
    }
}
