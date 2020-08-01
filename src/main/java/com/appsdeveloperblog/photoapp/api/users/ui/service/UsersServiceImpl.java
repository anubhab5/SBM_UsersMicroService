package com.appsdeveloperblog.photoapp.api.users.ui.service;

import java.util.ArrayList;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.photoapp.api.users.data.UserEntity;
import com.appsdeveloperblog.photoapp.api.users.data.UsersRepository;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;

@Service
public class UsersServiceImpl implements UserService {

//	@Autowired
	UsersRepository userRepo;

//	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UsersServiceImpl(UsersRepository userRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepo = userRepo;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public UserDto createUser(UserDto userDetails) {
		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
		userRepo.save(userEntity);

		UserDto returnValue = modelMapper.map(userEntity, UserDto.class);

		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepo.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(user.getEmail(), user.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
	}

}
