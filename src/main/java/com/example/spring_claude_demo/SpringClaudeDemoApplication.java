package com.example.spring_claude_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@RestController
@OpenAPIDefinition(
    info = @Info(
        title = "Employee Management API",
        version = "1.0",
        description = "API documentation for Employee Management System"
    )
)
public class SpringClaudeDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringClaudeDemoApplication.class, args);
	}
	
	@GetMapping("/")
	public String home() {
		return "Application is running!";
	}
}
