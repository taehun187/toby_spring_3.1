package com.taehun.SpringFrame.Composing_Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigA {

	@Bean
	public Aaa aaa() {
		Aaa aaa = new Aaa();
		return aaa;
	}
}  
