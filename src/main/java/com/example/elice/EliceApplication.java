package com.example.elice;

import com.example.elice.common.scheduled.FortuneCookieScheduler;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
		servers = {
				@Server(url = "https://beyrrnfbzxdpsfjk.tunnel-pt.elice.io/", description = "Default Server url")
		}
)
@EnableScheduling
@SpringBootApplication
public class EliceApplication {

	private final FortuneCookieScheduler fortuneCookieScheduler;
	@Autowired
	public EliceApplication(FortuneCookieScheduler fortuneCookieScheduler) {
		this.fortuneCookieScheduler = fortuneCookieScheduler;
	}

	public static void main(String[] args) {
		SpringApplication.run(EliceApplication.class, args);
	}

	@PostConstruct
	public void init() {
		fortuneCookieScheduler.fetchFortuneCookie();
	}
}
