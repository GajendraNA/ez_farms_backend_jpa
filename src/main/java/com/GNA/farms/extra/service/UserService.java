package com.GNA.farms.extra.service;

import com.example.quizApp.models.User;
import com.example.quizApp.models.UserRole;

import java.util.Set;

public interface UserService {

	//Creating User
	public User createUser(User user,Set<UserRole>userRoles) throws Exception;
	//get user by username
	public User getUser(String username);
	//delete user by id
	public void deleteUser(Long userId);
	// Get all user details
    public Set<User> getAllUsers();
	Set<User> getAllAdmins();
}
