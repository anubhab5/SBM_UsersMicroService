package com.appsdeveloperblog.photoapp.api.users.ui.controllers;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;
import com.appsdeveloperblog.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.appsdeveloperblog.photoapp.api.users.ui.model.CreateUserResponseModel;
import com.appsdeveloperblog.photoapp.api.users.ui.service.UserService;

@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private Environment env;

	@Autowired
	UserService userSvc;

	@GetMapping("/status/check")
	public String status() {
		return "Working on port " + env.getProperty("local.server.port");
	}

	@PostMapping
	public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetail) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserDto userDto = modelMapper.map(userDetail, UserDto.class);
		UserDto createdUser = userSvc.createUser(userDto);		
		CreateUserResponseModel retValue = modelMapper.map(createdUser, CreateUserResponseModel.class);		
		return ResponseEntity.status(HttpStatus.CREATED).body(retValue);
	}
}
