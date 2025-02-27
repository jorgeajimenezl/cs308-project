package com.example.cs308;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.cs308.spring.AppConfig;

public class Cs308ProjectApplication {

	public static void main(String[] args) {
		 ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
	}

}
