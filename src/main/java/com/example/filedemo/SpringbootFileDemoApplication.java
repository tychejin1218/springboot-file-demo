package com.example.filedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.filedemo.property.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class SpringbootFileDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootFileDemoApplication.class, args);
	}
}
