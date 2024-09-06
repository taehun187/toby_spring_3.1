package com.taehun.springframe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taehun.springframe.domain.User;

@Service
@Transactional
public interface UserService {
//	@Transactional
	void add(User user);
//	@Transactional
	void deleteAll();
//	@Transactional
	void update(User user);
	
	@Transactional(readOnly = true)
	Optional<User> get(String id);	
	
//	@Transactional(readOnly = true)
	List<User> getAll();
	
//	@Transactional
	void upgradeLevels();
}