package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SparkApplication {

	public static void main(String[] args) {

		SpringApplication springApplication = new SpringApplication(SparkApplication.class);
		//支持生成 pid 文件
		springApplication.addListeners(new ApplicationPidFileWriter("demo.pid"));
		springApplication.run(SparkApplication.class, args);
	}

}

