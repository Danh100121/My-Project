package com.javamaster.project2;

import java.util.Locale;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@SpringBootApplication
@EnableJpaAuditing  //// gen thời gian
@EnableScheduling /// đặt lịch
public class Project2Application implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(Project2Application.class, args);
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(new Locale("en"));
		return slr;
	}
	@Bean
	NewTopic noNewTopic() {
		//topic name , partition numbers, replication number
		return new NewTopic("notification", 2, (short) 1);
	}
//	@Bean
//	NewTopic statistic() {
//		return new NewTopic("statistic",1,(short) 1);
//	}
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");// cau hinh params tham so gui len de doi ngon ngu
		return lci;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

}