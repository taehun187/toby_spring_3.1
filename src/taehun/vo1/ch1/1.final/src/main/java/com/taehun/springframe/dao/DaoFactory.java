package com.taehun.springframe.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource; 
		// 드라이브 매니저보다 더 심플하기 때문에 데이터소스 사용  

@Configuration
public class DaoFactory {
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource ();

		dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class); // jdbc api 구체들  
		dataSource.setUrl("jdbc:mysql://localhost/sbdt_db?characterEncoding=UTF-8");
		dataSource.setUsername("root");
		dataSource.setPassword("12341234");

		return dataSource;
	}
	
	@Bean
	public DataSource dataSourceH() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource ();

		dataSource.setDriverClass(org.h2.Driver.class);
		dataSource.setUrl("\"jdbc:h2:tcp://localhost/~/test\"");
		dataSource.setUsername("root");
		dataSource.setPassword("12341234");

		return dataSource;
	}

	@Bean
	public UserDao userDao() {
		UserDao userDao = new UserDao();
		userDao.setDataSource(dataSource());
		return userDao;
	}
}
