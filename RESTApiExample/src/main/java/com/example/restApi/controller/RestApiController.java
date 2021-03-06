package com.example.restApi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.restApi.modal.User;
import com.example.restApi.service.UserService;
import com.example.restApi.util.CustomErrorType;

@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
@RequestMapping("/api")
public class RestApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestApiController.class);

	@Autowired
	UserService userService;

	
	
	// -------------------Retrieve All Users
	@RequestMapping(value = "/user/", method = RequestMethod.GET)
	public ResponseEntity<List<User>> listAllUsers() {
		LOGGER.info("listAllUsers start");
		List<User> users = userService.findAllUsers();
		if (users.isEmpty()) {
			LOGGER.info("listAllUsers end without users");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		LOGGER.info("listAllUsers End");
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	
	
	// -------------------Retrieve Single User
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> getUser(@PathVariable("id") long id) {
		LOGGER.info("getUser start");
		User user = userService.findById(id);
		if (user == null ) {
			LOGGER.info("getUser end without user");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		LOGGER.info("getUser End");
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	
	
	// -------------------Create a User
	@RequestMapping(value = "/user/", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
		LOGGER.info("Creating User : {}", user);
		if (userService.isUserExist(user)) {
			LOGGER.error("Unable to create. A User with name {} already exist", user.getName());
			return new ResponseEntity(new CustomErrorType("Unable to create. A User with name " + 
					user.getName() + " already exist."),HttpStatus.CONFLICT);
		}
		userService.saveUser(user);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}


	// ------------------- Update a User
	@RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {
		LOGGER.info("Updating User with id {}", id);	
		User currentUser = userService.findById(id);
		if (currentUser == null) {
			LOGGER.error("Unable to update. User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. User with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentUser.setName(user.getName());
		currentUser.setAge(user.getAge());
		currentUser.setSalary(user.getSalary());

		userService.updateUser(currentUser);
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);
	}

	
	
	// ------------------- Delete a User
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
    	LOGGER.info("Fetching & Deleting User with id {}", id);
        User user = userService.findById(id);
        if (user == null) {
        	LOGGER.error("Unable to delete. User with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to delete. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        userService.deleteUserById(id);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }

    
    
    // ------------------- Delete All Users
    @RequestMapping(value = "/user/", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteAllUsers() {
    	LOGGER.info("Deleting All Users");
        userService.deleteAllUsers();
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }
 
}

