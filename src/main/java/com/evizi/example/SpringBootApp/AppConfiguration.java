package com.evizi.example.SpringBootApp;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableCaching
@Import({ DBConfiguration.class })
public class AppConfiguration {

}
