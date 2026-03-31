package com.ecommerce.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.hibernate.SpringSessionContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/greet")
@RestController
public class HelloController {

    @GetMapping("/hello")
    ResponseEntity<String> greet()
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body("Hello :"+ SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
