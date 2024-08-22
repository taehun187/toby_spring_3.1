package com.taehun.springframe.dao;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import com.taehun.springframe.domain.User;

public interface UserDao {
	
	void setDataSource(DataSource dataSource);
	
	void add(User user);

	Optional<User> get(String id);

	List<User> getAll();

	void deleteAll();

	int getCount();	
	
	void update(User user);

}
