package com.vita.vitapickBack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//=> 스프링에서 설정화일로 인식
//=> 기본적인 Bean 설정용
@Configuration
public class VitapickBackConfig {

		//** PasswordEncoder 적용시	
		//=> dependency "spring-boot-starter-security" 를 추가하면,
		//   - SpringBoot 에서는 시큐리티가 자동실행 되므로 이를 제외시켜주어야함.
		//   - main 이 있는 Demo02Application.java 에서 exclude 설정  
		//	     @SpringBootApplication(exclude={SecurityAutoConfiguration.class})	
		@Bean
		PasswordEncoder getPasswordEncoder() {
			return new BCryptPasswordEncoder();
		}
}
