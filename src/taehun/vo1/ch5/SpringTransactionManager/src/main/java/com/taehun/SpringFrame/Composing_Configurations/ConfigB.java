package com.taehun.SpringFrame.Composing_Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ConfigA.class)
public class ConfigB {

	@Bean
	public Bbb bbb() {
		Bbb bbb = new Bbb();
		return bbb;
	}
}                                        