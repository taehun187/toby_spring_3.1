package com.taehun.SpringFrame.Trans;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class RepositoryConfig {

	@Autowired
	private DataSource dataSource;

	@Bean
//    public AccountRepository accountRepository(DataSource dataSource) {
//        return new JdbcAccountRepository(dataSource);
//    }
	public AccountRepository accountRepository() {
		return new JdbcAccountRepository(dataSource);
	}

	@PostConstruct
	public void init() {
		System.out.println("RepositoryConfig::init");
	}

}
