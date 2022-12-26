package com.edu.graduationproject.controller.rest;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.ui.Model;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.ui.Model;

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
import com.edu.graduationproject.entity.UserRole;
import com.edu.graduationproject.exception.BadRequestException;
import com.edu.graduationproject.exception.ResourceNotFoundException;
import com.edu.graduationproject.model.EAuthProvider;
import com.edu.graduationproject.service.UserRoleService;
import com.edu.graduationproject.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

@CrossOrigin("*")
@RestController
public class UserRestController {
	@Autowired
	UserService userService;
	@Autowired
	UserRoleService userRoleService;

	@GetMapping("/rest/users")
	public ResponseEntity<List<User>> getAccounts(@RequestParam("admin") Optional<Boolean> admin) {
		if (admin.orElse(false)) {
			return ResponseEntity.ok(userService.getAdministators());
		}
		return ResponseEntity.ok(userService.findAllIsDeletedFalse());
	}

	@GetMapping("/rest/users/principal")
	public ResponseEntity<Object> getAuthenticatedUser(Authentication authentication) {
		Map<String, Object> map = new HashMap<String, Object>();
		Collection<? extends GrantedAuthority> userRoles = authentication.getAuthorities();
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
			map.put("roles", userRoles);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/rest/users/{idOrUsername}")
	public ResponseEntity<User> getUserByUsername(@PathVariable("idOrUsername") Optional<Object> idOrUsername) {
		try {
			Optional<User> user = userService.findByUsername((String) idOrUsername.get());
			boolean isExist = false;
			if (!user.isPresent()) {
				user = userService.findById(Integer.parseInt((String) idOrUsername.get()));
				if (user.isPresent()) {
					if (user.get().getIsDeleted() == false) {
						isExist = true;
					}
				}
			} else {
				isExist = true;
			}
			if (isExist == true) {
				return ResponseEntity.ok(user.get());
			}
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	// admin
	@PostMapping("/rest/users")
	public ResponseEntity<User> create(@RequestBody User user) {
		Optional<User> findUser = userService.findByUsername(user.getUsername());
		boolean isExist = false;
		if (findUser.isPresent()) {
			if (findUser.get().getIsDeleted() == false) {
				isExist = true;
			}
		}
		if (isExist == true) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(userService.save(user));
	}

	// update user
	@PutMapping("/rest/users/{idOrUsername}")
	public ResponseEntity<User> update(@PathVariable("idOrUsername") Optional<Object> idOrUsername,
			@RequestBody User user, Model model, Authentication authentication) throws JsonProcessingException {
		try {
			Optional<User> loggedinUser = userService.findByUsername(authentication.getName());
			Optional<User> existingUser = userService.findByUsername((String) idOrUsername.get());
			Optional<User> existUserByEmail = userService.findByEmail(user.getEmail());
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
			if (existUserByEmail.isPresent()) {
				if (loggedinUser.get().getId() != existUserByEmail.get().getId()) {
					System.out.printf("Email đã được sử dụng");
					return ResponseEntity.badRequest().build();
				}
			}
			User savedUser = userService.update(user);
			return ResponseEntity.ok(savedUser);
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		} finally {
			model.asMap().clear();
		}
	}

	// update admin
	@PutMapping("/rest/admin/{idOrUsername}")
	public ResponseEntity<User> adminUpdate(@PathVariable("idOrUsername") Optional<Object> idOrUsername,
			@RequestBody User user, Authentication auth) throws JsonProcessingException {
		try {
			Optional<User> userByUsername = userService.findByUsername((String) idOrUsername.get());
			Optional<User> userById = userService.findById(Integer.valueOf((String) idOrUsername.get()));
			if (userByUsername.isPresent() || userById.isPresent()) {
				if (user.getIsDeleted() == null) {
					user.setIsDeleted(false);
				}
				User savedUser = userService.update(user);
				return ResponseEntity.ok(savedUser);
			}
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	// admin
	@DeleteMapping("/rest/users/{username}")
	public void delete(@PathVariable("username") Optional<String> username, Authentication auth) {
		try {
			Optional<User> reqUser = userService.findByUsername(auth.getName());
			Optional<User> findUser = userService.findByUsername(username.get());
			boolean isExist = false;
			if (findUser.isPresent() && reqUser.isPresent()) {
				isExist = true;
			}
			if (isExist == true) {
				if (auth.getName().equals(username.get())) {
					throw new BadRequestException("You can't delete yourself");
				}
				boolean isReqUserStaff = auth.getAuthorities().stream()
						.map(role -> role.getAuthority().replace("ROLE_", "")).collect(Collectors.toList())
						.contains("STAFF");
				if (isReqUserStaff) {
					boolean isFindUserAdmin = findUser.get().getAuthorities().stream()
							.map(role -> role.getRole().getName()).collect(Collectors.toList()).contains("ADMIN");
					if (isFindUserAdmin) {
						throw new BadRequestException("You can't delete admin");
					}
				}
				userService.deleteByUsername(username.get());
			} else {
				throw new ResourceNotFoundException("User", "username", username.get());
			}
		} catch (Exception e) {
			throw new ResourceNotFoundException("User", "username", username.get());
		}
	}

}