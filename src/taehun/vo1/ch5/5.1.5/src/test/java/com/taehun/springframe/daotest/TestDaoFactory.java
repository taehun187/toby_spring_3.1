package com.taehun.springframe.daotest;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.taehun.springframe.dao.UserDao;
import com.taehun.springframe.dao.UserDaoJdbc;
import com.taehun.springframe.service.UserService;

@Configuration
public class TestDaoFactory {
	
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource ();

		dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/sbdt_db?characterEncoding=UTF-8");
		dataSource.setUsername("root");
		dataSource.setPassword("12341234");

		return dataSource;
	}

	@Bean
    public UserDao userDao() {
        UserDaoJdbc userDaoJdbc = new UserDaoJdbc();  // UserDaoJdbc 구현체 사용
        userDaoJdbc.setDataSource(dataSource());
        return userDaoJdbc; 
    }
	
	@Bean
	public UserService userService() {
		UserService userService = new UserService();
		
		return userService;
	}
}
