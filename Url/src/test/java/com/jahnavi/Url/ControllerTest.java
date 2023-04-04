package com.jahnavi.Url;

import java.net.http.HttpResponse;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;

import com.jahnavi.Url.bean.Url;
import com.jahnavi.Url.bean.UrlDto;
import com.jahnavi.Url.bean.UrlReport;
import com.jahnavi.Url.controller.UrlReactiveController;
import com.jahnavi.Url.service.ReactiveService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {
	@Mock
	private ReactiveService service;
	
	@InjectMocks
	private UrlReactiveController controller;
	ServerHttpResponse response;
	
	
	@Test
	public void generateTest() {
		UrlDto urldto=new UrlDto("jahnavi","https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/");
		String shorturl="http://localhost:8080/miniurl.com/647980g8";
		Mockito.when(service.generate(urldto)).thenReturn(Mono.just(shorturl));
		
		Mono<String> result=controller.generate(urldto,response);
		StepVerifier.create(result)
			.expectNext(shorturl)
			.verifyComplete();
	}

	@Test
	public void redirectTest() {
		String shorturl="38g2h789";
		String longurl="https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/";
		Mockito.when(service.redirect(shorturl)).thenReturn(Mono.just(longurl));
		Mono<Void> result=controller.redirect(shorturl,response);
		StepVerifier.create(result)
			.expectComplete();
	}
	
	@Test
	public void getAllByDateTest() {
		String shorturl="38g2h789";
		LocalDate date=LocalDate.now();
		UrlReport urlreport=new UrlReport("1",shorturl,date,date,0);
		Flux<UrlReport> report=Flux.just(urlreport);
		Mockito.when(service.getAllByDate(date)).thenReturn(report);
		Flux<UrlReport> result=controller.getAllByDate(date,response);
		StepVerifier.create(result)
			.expectNext(urlreport)
			.expectComplete()
			.verify();
	}
	
	@Test
	public void getAllByHitTest() {
		String shorturl="38g2h789";
		LocalDate date=LocalDate.now();
		UrlReport urlreport=new UrlReport("1",shorturl,date,date,1);
		Flux<UrlReport> report=Flux.just(urlreport);
		Mockito.when(service.getAllByHit(date)).thenReturn(report);
		Flux<UrlReport> result=controller.getAllByHit(date,response);
		StepVerifier.create(result)
			.expectNext(urlreport)
			.expectComplete()
			.verify();
	}
	
//	@Test
//	public void generateerrorTest() {
//		HttpResponse<Url> mockresponse=Mockito.mock(HttpResponse.class);
////		Mockito.when(mockresponse.getCode()).thenReturn(500);
////		Mockito.when(mockresponse.getBody()).thenReturn(someSerialisedFormOfYourCarArray);
//
//		UrlDto urldto=new UrlDto("jahnavi","https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/");
//		String shorturl="http://localhost:8080/miniurl.com/647980g8";
//		Mockito.when(service.generate(urldto)).thenreturn;
//		
//		Mono<String> result=controller.generate(urldto);
//		StepVerifier.create(result)
//		.expectNext(response.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
//		
//			;
//	}
}
