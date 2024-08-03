package com.GNA.farms.extra.controller;

import com.example.quizApp.models.Role;
import com.example.quizApp.models.User;
import com.example.quizApp.models.UserRole;
import com.example.quizApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	//creating user
	@PostMapping("/")
	public User createUser(@RequestBody User user) throws Exception
	{
//	 user.setProfile("default.img");
	 user.setPassword(passwordEncoder.encode(user.getPassword()));
	 Set<UserRole>roles=new HashSet<>();
	 
	 Role role=new Role();
	 role.setRoleId(45L);
	 role.setRoleName("USER");
	 
	 UserRole userRole=new UserRole();
	 userRole.setUser(user);
	 userRole.setRole(role);
	 
	 roles.add(userRole);
	 
	 return this.userService.createUser(user, roles);
	}
	@GetMapping("/users/all")
	@Secured("ADMIN")
	public ResponseEntity<?> getAllUsers()
	{
	return ResponseEntity.ok(this.userService.getAllUsers());
	}
	@GetMapping("/admins/all")
	@Secured("ADMIN")
	public ResponseEntity<?> getAllAdmins()
	{
	return ResponseEntity.ok(this.userService.getAllAdmins());
	}
	
	@GetMapping("/{username}")
	public User getUser(@PathVariable("username") String username)
	{
	return this.userService.getUser(username);
	}
	
	//delete user by userId
	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable("userId") Long userId)
	{
	this.userService.deleteUser(userId);
	}
	
	


}
