package com.umc.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class HelperApplication {

	Logger logger= LoggerFactory.getLogger(HelperApplication.class);
	@PostConstruct
	public void started(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		logger.info("현재 시각: {}",new Date());
	}
	public static void main(String[] args) {
		SpringApplication.run(HelperApplication.class, args);
	}

}
