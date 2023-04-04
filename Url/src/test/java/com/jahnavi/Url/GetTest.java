package com.jahnavi.Url;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.jahnavi.Url.bean.ErrorDetails;
import com.jahnavi.Url.bean.Url;
import com.jahnavi.Url.bean.UrlDto;
import com.jahnavi.Url.bean.UrlReport;
import com.jahnavi.Url.exception.ArgumentNotValidException;
import com.jahnavi.Url.exception.UrlLengthException;
import com.jahnavi.Url.exception.UrlNotFoundException;
import com.jahnavi.Url.exception.UrlTimeoutException;
import com.jahnavi.Url.repository.UrlReportRepository;
import com.jahnavi.Url.repository.UrlRepository;
import com.jahnavi.Url.service.ReactiveService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class GetTest extends UrlApplicationTests{
	@Mock
	private UrlRepository repo;
	
	@Mock
	private UrlReportRepository reportrepo;
	
	@InjectMocks
	private ReactiveService service;
	
	@Test
	public void containsminiurlTest() {
		String mockShortUrl="67g";
		
		Mockito.when(repo.findByShortUrl(mockShortUrl)).thenReturn(Mono.empty());
		
		Mono<String> longurl=service.redirect(mockShortUrl);
		StepVerifier.create(longurl)
			.verifyError(UrlLengthException.class);
	}
	
	@Test
	public void urlnotfoundTest() {
		String mockShortUrl="489hg945";
		
		Mockito.when(repo.findByShortUrl(mockShortUrl)).thenReturn(Mono.empty());
		
		Mono<String> longurl=service.redirect(mockShortUrl);
		StepVerifier.create(longurl)
			.verifyError(UrlNotFoundException.class);
	}
	
	@Test
	public void urlfoundbutexpiredTest() {
		String mockShortUrl="489hg945";
		Url mockurl=new Url("1","jahnavi","https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/"
				,mockShortUrl,LocalDateTime.now(),LocalDateTime.now().minusSeconds(5));
		
		Mockito.when(repo.findByShortUrl(mockShortUrl)).thenReturn(Mono.just(mockurl));
		Mockito.when(repo.delete(mockurl)).thenReturn(Mono.empty());
		
		Mono<String> longurl=service.redirect(mockShortUrl);
		StepVerifier.create(longurl)
			.verifyError(UrlTimeoutException.class);
	}

	@Test
	public void urlfoundnotexpiredTest() {
		String mockShortUrl="489hg945";
		String mocklongurl="https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/";
		Url mockurl=new Url("1","jahnavi","https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/"
				,mockShortUrl,LocalDateTime.now(),LocalDateTime.now().plusSeconds(10));
		UrlReport urlreport=new UrlReport("1",mockShortUrl,LocalDate.now(),LocalDate.now(),0);
		UrlReport savedreport=new UrlReport("1",mockShortUrl,LocalDate.now(),LocalDate.now(),1);
		
		Mockito.when(repo.findByShortUrl(mockShortUrl)).thenReturn(Mono.just(mockurl));
		Mockito.when(reportrepo.findByShortUrlAndDate(mockShortUrl,LocalDate.now())).thenReturn(Mono.just(urlreport));
		Mockito.when(reportrepo.save(Mockito.any(UrlReport.class))).thenReturn(Mono.just(savedreport));
		
		Mono<String> longurl=service.redirect(mockShortUrl);
		StepVerifier.create(longurl)
			.expectNext(mocklongurl)
			.verifyComplete();
	}
	
	@Test
	public void getbydateTest() {
		LocalDate date=LocalDate.now();
		String shorturl="38g2h789";
		UrlReport urlreport=new UrlReport("1",shorturl,date,date,0);
		Flux<UrlReport> urlresult=Flux.just(urlreport);
		Mockito.when(reportrepo.findByCreateDateAndDate(date, date)).thenReturn(Flux.just(urlreport));
		Flux<UrlReport> result=service.getAllByDate(date);
		StepVerifier.create(result)
			.expectNext(urlreport)
			.expectComplete()
			.verify();
	}
	
	
	@Test
	public void getbyhitTest() {
		LocalDate date=LocalDate.now();
		String shorturl="38g2h789";
		UrlReport urlreport=new UrlReport("1",shorturl,date,date,1);
		Flux<UrlReport> urlresult=Flux.just(urlreport);
		Mockito.when(reportrepo.findByDate(date)).thenReturn(Flux.just(urlreport));
		Flux<UrlReport> result=service.getAllByHit(date);
		StepVerifier.create(result)
			.expectNext(urlreport)
			.expectComplete()
			.verify();
	}
	
	
	
//	@Autowired
//	private WebClient webClient;
	
	//if short url does not contain miniurl
//	@Test
//	public void Test1() {
//		Mono<String> result=this.webClient
//				.get()
//				.uri("{shortUrl}","www.google.com")
//				.retrieve()
//				.bodyToMono(String.class)
//				;
//		StepVerifier.create(result)
//		.verifyError(WebClientResponseException.BadRequest.class);
//	}
//	
//	//not found in db
//	@Test
//	public void Test2() {
//		Mono<String> result=this.webClient
//				.get()
//				.uri("{shortUrl}","miniurl-374692")
//				.retrieve()
//				.bodyToMono(String.class)
//				;
//		StepVerifier.create(result)
//		.verifyError(WebClientResponseException.NotFound.class);
//	}
//	
//	
//	//is redirected?
//		@Test
//		public void Test3() {
//			Mono<ResponseEntity<Void>> result=this.webClient
//					.get()
//					.uri("miniurl-3e5a8a5f")
//					.retrieve()
//					.toBodilessEntity();
//			StepVerifier.create(result)
//			.expectNextMatches(u->u.getStatusCode().equals(HttpStatus.PERMANENT_REDIRECT))
//			.verifyComplete();
//		}
//	
//	
//	//expired
//		@Test
//		public void Test4() {
//			Mono<String> result=this.webClient
//					.get()
//					.uri("miniurl-235e3bf2")
//					.retrieve()
//					.bodyToMono(String.class)
//					;
//			StepVerifier.create(result)
//			.verifyError(WebClientResponseException.GatewayTimeout.class);
//		}
}
