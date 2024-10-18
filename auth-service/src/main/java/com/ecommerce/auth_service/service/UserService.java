package com.ecommerce.auth_service.service;


import com.ecommerce.auth_service.dto.request.UserCreateRequest;
import com.ecommerce.auth_service.entity.User;
import com.ecommerce.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserCreateRequest userCreateRequest){
        User user = new User();
        user.setFirstName(userCreateRequest.getFirstName());
        user.setLastName(userCreateRequest.getLastName());
        user.setUsername(userCreateRequest.getUsername());
        user.setPassword(userCreateRequest.getPassword());
        user.setDateOfBirth(userCreateRequest.getDateOfBirth());
        userRepository.save(user);
        return user;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }
}
