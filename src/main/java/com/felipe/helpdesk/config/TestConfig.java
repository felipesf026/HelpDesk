package com.felipe.helpdesk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.felipe.helpdesk.services.DBService;

@Configuration
@Profile("test") 
public class TestConfig {
	
	@Autowired
	private DBService dbService;
	
	@Bean
	public void instaciaDB() {
		this.dbService.instaciaDB();
	}

}
