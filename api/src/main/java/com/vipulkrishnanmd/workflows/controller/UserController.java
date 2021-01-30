package com.vipulkrishnanmd.workflows.controller;

import javax.validation.Valid;

import com.vipulkrishnanmd.workflows.exception.ResourceNotFoundException;
import com.vipulkrishnanmd.workflows.model.User;
import com.vipulkrishnanmd.workflows.repo.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/user/v1")
public class UserController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepo userRepo;

    /**
     * Create a new user.
     * 
     * @param user
     * @return
     */
    @PostMapping("/user")
    public User createUser(@Valid @RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    /**
     * Get user by ID
     * 
     * @param userId
     * @return
     * @throws ResourceNotFoundException
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUsersById(@PathVariable(value = "id") Long userId)
        throws ResourceNotFoundException {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
        return ResponseEntity.ok().body(user);
    }
}
