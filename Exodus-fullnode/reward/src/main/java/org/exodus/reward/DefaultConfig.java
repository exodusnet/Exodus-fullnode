package org.exodus.reward;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "app")
@Configuration
@Import(CommonConfig.class)
public class DefaultConfig {

}
