package io.flowingretail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FlowingRetailApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowingRetailApplication.class, args);
	}

}
