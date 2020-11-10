package com.idealista.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfiguration extends WebSecurityConfigurerAdapter {

	private static String PUBLIC_AD_ENDPOINT = "/api/ads/public";
	private static String QUALITY_USER = "qualityAdmin";
	private static String QUALITY_PASSWORD = "1234";
	private static String QUALITY_USER_ROL = "ADMIN_QUALITY";

	Logger logger = LoggerFactory.getLogger(AuthConfiguration.class);

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		logger.info("[CONFIGURATION] Enabling HTTP Security configuration");
		httpSecurity
			.csrf()
				.disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.GET, PUBLIC_AD_ENDPOINT)
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.formLogin()
			.permitAll()
			.and().httpBasic();
	}

	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		logger.info("[CONFIGURATION] Creating quality user in memory auth");
		auth.inMemoryAuthentication()
			.withUser(QUALITY_USER)
			.password(getPasswordEnconder().encode(QUALITY_PASSWORD))
			.roles(QUALITY_USER_ROL);
	}

	@Bean
	public PasswordEncoder getPasswordEnconder() {
		return new BCryptPasswordEncoder();
	}
}
