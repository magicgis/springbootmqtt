package com.koakh.mqtt.moquette.datalibrary.config;

/**
 * Spring Boot: autowire beans from library project
 * https://stackoverflow.com/questions/41815871/spring-boot-autowire-beans-from-library-project
 *
 * Trick for Mongo is use @EnableMongoRepositories("hello.service") else it gives error
 * Parameter 0 of constructor in hello.app.DemoApplication required a bean of type 'hello.service.UserRepository' that could not be found.
 * Cause some pain hours to figure whats appens find and initialize @beans
 *
 * The FINAL trick is have this class in context and use DEFAULT
 * @SpringBootApplication(scanBasePackages = "com.koakh.mqtt.moquette") in Applications
 *
 * if we remove this class it Crashes trying to find @Beans on Boot
 *
 * Optionnaly we can use @Import(SharedConfigurationReference.class) in Application,
 * but not needed is more cleaner have only this @Configuration bean in Spring Context
 */
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@ComponentScan("com.koakh.mqtt.moquette")
@EnableMongoRepositories("com.koakh.mqtt.moquette")
public class SharedConfigurationReference {
}
