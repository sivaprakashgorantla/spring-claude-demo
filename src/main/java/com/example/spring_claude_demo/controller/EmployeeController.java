package com.example.spring_claude_demo.controller;

import com.example.spring_claude_demo.exception.ResourceNotFoundException;
import com.example.spring_claude_demo.model.Employee;
import com.example.spring_claude_demo.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee", description = "Employee management APIs")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Create a new employee", description = "Creates a new employee and returns the created employee details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "400", description = "Invalid employee data provided",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @Parameter(description = "Employee object to be created", required = true) 
            @RequestBody Employee employee) {
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all employees", description = "Returns a list of all employees in the system")
    @ApiResponse(responseCode = "200", description = "List of employees retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class)))
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Operation(summary = "Get employee by ID", description = "Returns a single employee identified by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @Parameter(description = "ID of the employee to retrieve", required = true)
            @PathVariable Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Operation(summary = "Get employees by last name", description = "Returns a list of employees with the specified last name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "204", description = "No employees found with the specified last name",
                    content = @Content)
    })
    @GetMapping("/lastName/{lastName}")
    public ResponseEntity<List<Employee>> getEmployeesByLastName(
            @Parameter(description = "Last name to search for", required = true)
            @PathVariable String lastName) {
        List<Employee> employees = employeeService.getEmployeesByLastName(lastName);
        if (employees.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Operation(summary = "Get employees by position", description = "Returns a list of employees with the specified position")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "204", description = "No employees found with the specified position",
                    content = @Content)
    })
    @GetMapping("/position/{position}")
    public ResponseEntity<List<Employee>> getEmployeesByPosition(
            @Parameter(description = "Position to search for", required = true)
            @PathVariable String position) {
        List<Employee> employees = employeeService.getEmployeesByPosition(position);
        if (employees.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Operation(summary = "Get employees by email pattern", description = "Returns a list of employees whose email contains the specified text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "204", description = "No employees found with the specified email pattern",
                    content = @Content)
    })
    @GetMapping("/email")
    public ResponseEntity<List<Employee>> getEmployeesByEmailContaining(
            @Parameter(description = "Text to search for in email addresses", required = true)
            @RequestParam String contains) {
        List<Employee> employees = employeeService.getEmployeesByEmailContaining(contains);
        if (employees.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Operation(summary = "Get employees by minimum salary", description = "Returns a list of employees with salary equal to or greater than the specified amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "204", description = "No employees found with the specified minimum salary",
                    content = @Content)
    })
    @GetMapping("/salary")
    public ResponseEntity<List<Employee>> getEmployeesByMinimumSalary(
            @Parameter(description = "Minimum salary threshold", required = true)
            @RequestParam Double minSalary) {
        List<Employee> employees = employeeService.getEmployeesByMinimumSalary(minSalary);
        if (employees.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Operation(summary = "Update an employee", description = "Updates an existing employee identified by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid employee data provided",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @Parameter(description = "ID of the employee to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated employee details", required = true)
            @RequestBody Employee employeeDetails) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @Operation(summary = "Delete an employee", description = "Deletes an employee identified by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee deleted successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(
            @Parameter(description = "ID of the employee to delete", required = true)
            @PathVariable Long id) {
        employeeService.deleteEmployee(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}