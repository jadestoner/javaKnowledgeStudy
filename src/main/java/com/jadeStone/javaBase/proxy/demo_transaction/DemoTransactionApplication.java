/**
 * 
 */
package com.jadeStone.javaBase.proxy.demo_transaction;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 
 *
 */
@SpringBootApplication
public class DemoTransactionApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(DemoTransactionApplication.class);
	    app.setBannerMode(Banner.Mode.OFF);
	    app.run(args);
	}
}
