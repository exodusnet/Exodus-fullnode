package org.exodus.reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "app")
@Configuration
@ComponentScan(basePackages = "org.exodus.reward")
public class CommonConfig {

	@Autowired
	public ApplicationContext appCtx;

	public CommonConfig() {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> log.error("Uncaught exception", e));
	}

}
