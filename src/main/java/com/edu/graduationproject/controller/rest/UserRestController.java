package com.edu.graduationproject.controller.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.graduationproject.entity.User;
import com.edu.graduationproject.exception.ResourceNotFoundException;
import com.edu.graduationproject.model.EAuthProvider;
import com.edu.graduationproject.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

@CrossOrigin("*")
@RestController
public class UserRestController {
    @Autowired
    UserService userService;

    @GetMapping("/rest/users")
    public ResponseEntity<List<User>> getAccounts(@RequestParam("admin") Optional<Boolean> admin) {
        if (admin.orElse(false)) {
            return ResponseEntity.ok(userService.getAdministators());
        }
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/rest/users/principal")
    public ResponseEntity<Object> getAuthenticatedUser(Authentication authentication) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Optional<User> loggedinUser = userService.findByUsername(authentication.getName());
            map.put("id", loggedinUser.get().getId());
            map.put("username", loggedinUser.get().getUsername());
            map.put("phone", loggedinUser.get().getPhone());
            map.put("email", loggedinUser.get().getEmail());
            map.put("fullname", loggedinUser.get().getFullname());
            map.put("address", loggedinUser.get().getAddress());
            map.put("fullname", loggedinUser.get().getFullname());
            map.put("image_url", loggedinUser.get().getImage_url());
            return ResponseEntity.ok(map);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/rest/users/{idOrUsername}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("idOrUsername") Optional<Object> idOrUsername) {
        try {
            Optional<User> user = userService.findByUsername((String) idOrUsername.get());
            if (!user.isPresent()) {
                user = userService.findById(Integer.valueOf((String) idOrUsername.get()));
            }
            return ResponseEntity.ok(user.get());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // admin
    @PostMapping("/rest/users")
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    // update user
    @PutMapping("/rest/users/{idOrUsername}")
    public ResponseEntity<User> update(@PathVariable("idOrUsername") Optional<Object> idOrUsername,
            @RequestBody User user) throws JsonProcessingException {
        try {
            Optional<User> existingUser = userService.findByUsername((String) idOrUsername.get());
            if (!existingUser.isPresent()) {
                existingUser = userService.findById(Integer.valueOf((String) idOrUsername.get()));
            }
            User savedUser = userService.update(user);
            return ResponseEntity.ok(savedUser);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // update admin
    @PutMapping("/rest/admin/{idOrUsername}")
    public ResponseEntity<User> adminUpdate(@PathVariable("idOrUsername") Optional<Object> idOrUsername,
            @RequestBody User user) throws JsonProcessingException {
        try {
            Optional<User> existingUser = userService.findByUsername((String) idOrUsername.get());
            if (!existingUser.isPresent()) {
                existingUser = userService.findById(Integer.valueOf((String) idOrUsername.get()));
            }
            if (!existingUser.get().getProvider().equals(EAuthProvider.DATABASE)) {
                return ResponseEntity.badRequest().body(user);
            }
            if (user.getEnabled() == null) {
                user.setEnabled(true);
            }
            if (user.getProvider() != EAuthProvider.DATABASE) {
                user.setProvider(EAuthProvider.DATABASE);
            }
            user.setUpdatedAt(new Date());
            User savedUser = userService.update(user);
            return ResponseEntity.ok(savedUser);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // admin
    @DeleteMapping("/rest/users/{username}")
    public void delete(@PathVariable("username") Optional<String> username) {
        try {
            userService.deleteByUsername(username.get());
        } catch (Exception e) {
            throw new ResourceNotFoundException("User", "username", username.get());
        }
    }

}