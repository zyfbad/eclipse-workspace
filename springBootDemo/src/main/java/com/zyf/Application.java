package com.zyf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.zyf.dao")
@EntityScan("com.zyf.entity")
@MapperScan(basePackages="com.zyf.mapper")
@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		
		SpringApplication.run(Application.class, args);
		
	}
	
}
/**
	spring.activemq.broker-url=tcp://192.168.25.136:61616

*/