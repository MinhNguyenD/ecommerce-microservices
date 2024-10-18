package com.ecommerce.auth_service.controller;
import com.ecommerce.auth_service.dto.request.UserCreateRequest;
import com.ecommerce.auth_service.entity.User;
import com.ecommerce.auth_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequest userCreateRequest){
        return new ResponseEntity<>(userService.createUser(userCreateRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }
 }
