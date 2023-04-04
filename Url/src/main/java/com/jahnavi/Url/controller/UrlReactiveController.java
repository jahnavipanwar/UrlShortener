package com.jahnavi.Url.controller;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jahnavi.Url.bean.Url;
import com.jahnavi.Url.bean.UrlDto;
import com.jahnavi.Url.bean.UrlReport;
import com.jahnavi.Url.exception.ArgumentNotValidException;
import com.jahnavi.Url.repository.UrlReportRepository;
import com.jahnavi.Url.repository.UrlRepository;
import com.jahnavi.Url.service.ReactiveService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UrlReactiveController {
	@Autowired
	ReactiveService service;
	@Autowired
	UrlRepository repo;
	Logger logger = LoggerFactory.getLogger(UrlReactiveController.class);

	@PostMapping("/create")
	public Mono<String> generate(@RequestBody UrlDto urlDto,ServerHttpResponse response) {
		return this.service.generate(urlDto)
				.onErrorResume(throwable -> {
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			DataBuffer dataBuffer = response.bufferFactory()
					.wrap(throwable.getMessage().getBytes(StandardCharsets.UTF_8));
			return response.writeWith(Mono.just(dataBuffer)).then(Mono.empty());
		});
	}

	@GetMapping("/miniurl.com/{shortUrl}")
	public Mono<Void> redirect(@PathVariable String shortUrl,ServerHttpResponse response) {
		return this.service.redirect(shortUrl).doOnNext(msg -> logger.info("response received from service"))
				.flatMap(url -> {
					response.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
					response.getHeaders().setLocation(URI.create(url));
					return response.setComplete();
				})
				.onErrorResume(throwable -> {
					response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
					DataBuffer dataBuffer = response.bufferFactory()
							.wrap(throwable.getMessage().getBytes(StandardCharsets.UTF_8));
					return response.writeWith(Mono.just(dataBuffer)).then(Mono.empty());
				});

	}

	@GetMapping("/urlByCreateDate")
	public Flux<UrlReport> getAllByDate(@RequestParam LocalDate date,ServerHttpResponse response) {
		return service.getAllByDate(date).onErrorResume(throwable -> {
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			DataBuffer dataBuffer = response.bufferFactory()
					.wrap(throwable.getMessage().getBytes(StandardCharsets.UTF_8));
			return response.writeWith(Mono.just(dataBuffer)).then(Mono.empty());
		});
	}

	@GetMapping("/urlByHit")
	public Flux<UrlReport> getAllByHit(@RequestParam LocalDate date,ServerHttpResponse response) {
		return service.getAllByHit(date)
				.onErrorResume(throwable -> {
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			DataBuffer dataBuffer = response.bufferFactory()
					.wrap(throwable.getMessage().getBytes(StandardCharsets.UTF_8));
			return response.writeWith(Mono.just(dataBuffer)).then(Mono.empty());
		});
	}
	@GetMapping("/getreport")
	public Flux<UrlReport> getAll(ServerHttpResponse response) {
		return service.getAll().onErrorResume(throwable -> {
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			DataBuffer dataBuffer = response.bufferFactory()
					.wrap(throwable.getMessage().getBytes(StandardCharsets.UTF_8));
			return response.writeWith(Mono.just(dataBuffer)).then(Mono.empty());
		});
	}
}
