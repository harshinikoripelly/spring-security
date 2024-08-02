package com.taashee.spring_security.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.Getter;

@Configuration
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	private String durationInMinutes;
	private String secret;
	public String getDurationInMinutes() {
		return durationInMinutes;
	}
	public void setDurationInMinutes(String durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	
}