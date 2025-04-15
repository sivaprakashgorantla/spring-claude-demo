package com.example.spring_claude_demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Welcome", description = "Welcome and hello world APIs")
public class HelloWorldController {

    @Operation(summary = "Say hello", description = "Returns a simple hello world message")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
    
    @Operation(summary = "Welcome message", description = "Returns a welcome message with application information")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    @GetMapping("/")
    public String welcome() {
        return "Welcome to Spring Boot Hello World Application on port 8081!";
    }
}