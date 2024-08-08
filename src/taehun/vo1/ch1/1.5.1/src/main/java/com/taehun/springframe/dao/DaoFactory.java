package com.taehun.springframe.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// annotation based configuration metadata  어노테이션 기반의 컨피규레이션 메타데이타 사용 
@Configuration
public class DaoFactory {
	@Bean  
	public UserDao userDao() {
		UserDao dao = new UserDao(connectionMMaker()); // Dependent Inject
			                                          // : Constructor argument
		return dao;			// 일반적인 메서드 호출이 아니라 디펜던시 인젝션임 
	}

	@Bean
	public ConnectionMaker connectionMMaker() {
		ConnectionMaker connectionMaker = new MConnectionMaker();
		return connectionMaker;
	}
	
	@Bean
	public ConnectionMaker connectionHMaker() {
		ConnectionMaker connectionMaker = new HConnectionMaker();
		return connectionMaker;
	}
}
