package com.jahnavi.Url;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.jahnavi.Url.bean.ErrorDetails;
import com.jahnavi.Url.bean.Url;
import com.jahnavi.Url.bean.UrlDto;
import com.jahnavi.Url.bean.UrlReport;
import com.jahnavi.Url.controller.UrlReactiveController;
import com.jahnavi.Url.exception.ArgumentNotValidException;
import com.jahnavi.Url.exception.UrlLengthException;
import com.jahnavi.Url.repository.UrlReportRepository;
import com.jahnavi.Url.repository.UrlRepository;
import com.jahnavi.Url.service.ReactiveService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

//@RunWith(SpringRunner.class)
//@WebMvcTest(UrlReactiveController.class)
@ExtendWith(MockitoExtension.class)
public class PostTest extends UrlApplicationTests{
	@Mock
	private UrlRepository repo;
	
	@Mock
	private UrlReportRepository reportrepo;
	
	@InjectMocks
	private ReactiveService service;
	
	@Test
	public void blankTest(){
		UrlDto mockLongUrl=new UrlDto("","jahnavi");
		Mono<String> shortUrlResponse=service.generate(mockLongUrl).doOnNext(System.out::println);
		StepVerifier.create(shortUrlResponse)
		.verifyError(ArgumentNotValidException.class);
	}
	
	@Test
	public void containsminiurlTest(){
		UrlDto mockLongUrl=new UrlDto("www.miniurl.nxsic","jahnavi");
		Mono<String> shortUrlResponse=service.generate(mockLongUrl).doOnNext(System.out::println);
		StepVerifier.create(shortUrlResponse)
		.verifyError(ArgumentNotValidException.class);
	}
	
	@Test
	public void verifylengthTest(){
		UrlDto mockLongUrl=new UrlDto("www.google.com","jahnavi");
		Mono<String> shortUrlResponse=service.generate(mockLongUrl).doOnNext(System.out::println);
		StepVerifier.create(shortUrlResponse)
		.verifyError(UrlLengthException.class);
	}
	
	@Test
	public void notinbothdbTest() throws Exception{
		UrlDto mockLongUrl=new UrlDto("https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/","jahnavi");
//		String shorturl="http://localhost:8080/miniurl.com/29fh2014";
		String mockShortUrl="29fh2014";
		String mockresponse="UserId: "+mockLongUrl.getUserId()+"\n"+"ShortUrl: "+"http://localhost:8080/miniurl.com/"+mockShortUrl;
		Url mockurl=new Url("1","jahnavi",mockLongUrl.getLongUrl(),mockShortUrl,LocalDateTime.now(),LocalDateTime.now());
		UrlReport mockreport=new UrlReport("1",mockShortUrl,LocalDate.now(),mockurl.getCreation().toLocalDate(),0);
		
		Mockito.when(repo.findByUserIdAndLongUrl(mockLongUrl.getUserId(),mockLongUrl.getLongUrl())).thenReturn(Mono.empty());
		Mockito.when(repo.save(Mockito.any(Url.class))).thenReturn(Mono.just(mockurl));
		Mockito.when(reportrepo.findByShortUrl(mockurl.getShortUrl())).thenReturn(Mono.empty());
		Mockito.when(reportrepo.save(Mockito.any(UrlReport.class))).thenReturn(Mono.just(mockreport));
		
		Mono<String> response=service.generate(mockLongUrl);
		StepVerifier.create(response)
			.expectNext(mockresponse)
			.verifyComplete();
	}
	
	@Test
	public void inbothdbTest() throws Exception{
		UrlDto mockLongUrl=new UrlDto("https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/","jahnavi");
		String mockShortUrl="29fh2014";
		String mockresponse="UserId: "+mockLongUrl.getUserId()+"\n"+"ShortUrl: "+"http://localhost:8080/miniurl.com/"+mockShortUrl;
		
		Url mockurl=new Url("1","jahnavi",mockLongUrl.getLongUrl(),mockShortUrl,LocalDateTime.now(),LocalDateTime.now());
		UrlReport mockreport=new UrlReport("1",mockShortUrl,LocalDate.now(),mockurl.getCreation().toLocalDate(),0);
		
		Mockito.when(repo.findByUserIdAndLongUrl(mockLongUrl.getUserId(),mockLongUrl.getLongUrl())).thenReturn(Mono.just(mockurl));
		Mockito.when(repo.save(Mockito.any(Url.class))).thenReturn(Mono.just(mockurl));
		Mockito.when(reportrepo.findByShortUrl(mockurl.getShortUrl())).thenReturn(Mono.just(mockreport));
		Mockito.when(reportrepo.save(Mockito.any(UrlReport.class))).thenReturn(Mono.just(mockreport));
		
		Mono<String> response=service.generate(mockLongUrl);
		StepVerifier.create(response)
			.expectNext(mockresponse)
			.verifyComplete();
	}
	
	
	
	
//public class PostTest extends BaseTest{
	
//	UrlReactiveController c;
//	@BeforeEach
//	public void setup() {
//		c=new UrlReactiveController();
//	}
//	
//	@Test
//	public void test1() {
//		assertEquals(Mono.just(ArgumentNotValidException.class), c.generate(new UrlDto("","")));
//	}
	
//	@Autowired
//	private WebClient webClient;
//	
//	//check if short url contains "miniurl" for given long url
//	@Test
//	public void Test1() {
//		Mono<Url> result=this.webClient
//				.post()
//				.uri("create")
//				.bodyValue( new UrlDto("https://www.youtube.com",""))
//				.retrieve()
//				.bodyToMono(Url.class)
//				.doOnNext(System.out::println);
//		StepVerifier.create(result)
//		.expectNextMatches(u->u.getShortUrl().contains("miniurl"))
//		.verifyComplete();
//	}
//	
//	//empty long url
//	@Test
//	public void Test2() {
//		Mono<String> result=this.webClient
//				.post()
//				.uri("create")
//				.bodyValue( new UrlDto("",""))
//				.retrieve()
//				.bodyToMono(String.class)
//				;
//		StepVerifier.create(result)
//		.verifyError(WebClientResponseException.BadRequest.class);
//	}
//	
//	//long url has miniurl
//	@Test
//	public void Test3() {
//		Mono<String> result=this.webClient
//				.post()
//				.uri("create")
//				.bodyValue( new UrlDto("https://www.miniurl.com",""))
//				.retrieve()
//				.bodyToMono(String.class)
//				;
//		StepVerifier.create(result)
//		.verifyError(WebClientResponseException.BadRequest.class);
//	}
	
}
