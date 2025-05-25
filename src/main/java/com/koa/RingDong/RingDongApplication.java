package com.koa.RingDong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RingDongApplication {

	public static void main(String[] args) {
		SpringApplication.run(RingDongApplication.class, args);
	}

}
