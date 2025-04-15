package com.example.spring_claude_demo.config;

import com.example.spring_claude_demo.model.Employee;
import com.example.spring_claude_demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {
        return args -> {
            // Clear previous data
            repository.deleteAll();
            
            // Create sample employees
            Employee emp1 = new Employee(
                    "John", "Doe", "john.doe@example.com", "123-456-7890",
                    "Software Engineer", 85000.0, LocalDate.of(2020, 3, 15)
            );
            
            Employee emp2 = new Employee(
                    "Jane", "Smith", "jane.smith@example.com", "987-654-3210",
                    "Product Manager", 95000.0, LocalDate.of(2019, 6, 10)
            );
            
            Employee emp3 = new Employee(
                    "Robert", "Johnson", "robert.johnson@example.com", "555-123-4567",
                    "QA Engineer", 75000.0, LocalDate.of(2021, 1, 5)
            );
            
            Employee emp4 = new Employee(
                    "Emily", "Davis", "emily.davis@example.com", "444-333-2222",
                    "UX Designer", 82000.0, LocalDate.of(2022, 2, 20)
            );
            
            Employee emp5 = new Employee(
                    "Michael", "Brown", "michael.brown@example.com", "777-888-9999",
                    "DevOps Engineer", 92000.0, LocalDate.of(2018, 11, 12)
            );
            
            // Save sample employees
            repository.saveAll(Arrays.asList(emp1, emp2, emp3, emp4, emp5));
            
            System.out.println("Sample employee data has been initialized");
        };
    }
}
