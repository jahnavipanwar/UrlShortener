package com.jahnavi.Url.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;
import com.jahnavi.Url.bean.Url;
import com.jahnavi.Url.bean.UrlDto;
import com.jahnavi.Url.bean.UrlReport;
import com.jahnavi.Url.exception.ArgumentNotValidException;
import com.jahnavi.Url.exception.UrlLengthException;
import com.jahnavi.Url.exception.UrlNotFoundException;
import com.jahnavi.Url.exception.UrlTimeoutException;
import com.jahnavi.Url.repository.UrlReportRepository;
import com.jahnavi.Url.repository.UrlRepository;
import com.jahnavi.Url.repository.UserRepository;

import io.micrometer.common.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReactiveService {
	@Autowired
	UrlRepository repo;
	@Autowired
	UrlReportRepository reportrepo;
	@Autowired
	UserRepository userrepo;

	Logger logger = LoggerFactory.getLogger(ReactiveService.class);
	String domain="http://localhost:8080/miniurl.com/";
	

	// generates short url for given long url object(POST)
	public Mono<String> generate(UrlDto urlDto) {
		logger.info("Generate method invoked at service with " + urlDto);
		return Mono.just(urlDto)
				.filter(urldto -> !urldto.getLongUrl().isBlank() && !urldto.getLongUrl().contains("miniurl"))
				.switchIfEmpty(Mono.error(new ArgumentNotValidException("Please Provide Valid Long Url")))
				
				.filter(urldto->urldto.getLongUrl().length()>50)
				.switchIfEmpty(Mono.error(new UrlLengthException("Enter url of length atleast 50 characters")))
				
//				.flatMap(urldto->demo(urldto))
				
				.flatMap(urldto -> repo.findByUserIdAndLongUrl(urldto.getUserId(),urldto.getLongUrl())
						.switchIfEmpty(createUrl(urldto)))
				.flatMap(url -> reportrepo.findByShortUrl(url.getShortUrl())
						.switchIfEmpty(createUrlReport(url))
						.then(Mono.just("UserId: "+url.getUserId()+"\n"+"ShortUrl: "+domain+url.getShortUrl())))
				.doOnNext(msg -> logger.info("Url object returned successfully " + msg));
	}
	
	// Create UrlReport object via generate(hits=0)
	private Mono<UrlReport> createUrlReport(Url url) {
		
		return reportrepo
				.save((new UrlReport(null, url.getShortUrl(), LocalDate.now(), url.getCreation().toLocalDate(), 0)));
	}
	
	// create hash
	public String encodeUrl(String url) {
		String encodeUrl = "";
		LocalDateTime time = LocalDateTime.now();
		encodeUrl = Hashing.murmur3_32_fixed().hashString(url.concat(time.toString()), StandardCharsets.UTF_8).toString();
		return encodeUrl;
	}

	// To create object of Url
	public Mono<Url> createUrl(UrlDto urlDto) {
		Url url=new Url(null,urlDto.getUserId() ,urlDto.getLongUrl(),encodeUrl(urlDto.getLongUrl()),
				LocalDateTime.now(), LocalDateTime.now().plusDays(1));
		return repo.save(url);

	}

	// get Url object by short Url
	public Mono<Url> getEncodedUrl(String url) {
		return repo.findByShortUrl(url);
	}

	// delete after expiration
	public Mono<Void> deleteShortUrl(String shortUrl) {
		return repo.findByShortUrl(shortUrl).flatMap(u -> repo.delete(u));

	}

	// increase hits by 1 on redirect
	public Mono<UrlReport> updateHits(UrlReport urlReport) {
		urlReport.setHits(urlReport.getHits() + 1);
		return reportrepo.save(urlReport);

	}

	// create new UrlReport object on redirect(hits=1)
	public Mono<UrlReport> createNewUrlReport(Url url) {
		return reportrepo
				.save((new UrlReport(null, url.getShortUrl(), LocalDate.now(), url.getCreation().toLocalDate(), 1)));
	}

	// Redirecting To long url(GET)
	public Mono<String> redirect(String shortUrl) {
		return Mono.just(shortUrl).filter(url -> url.length()==8)
				.switchIfEmpty(Mono.error(new UrlLengthException("Please Provide Correct Short Url")))
				.flatMap(url -> getEncodedUrl(shortUrl)
						.switchIfEmpty(Mono.error(new UrlNotFoundException("short url does not exists"))))
				.filter(url -> url.getExpiration().isAfter(LocalDateTime.now()))
				.switchIfEmpty(deleteShortUrl(shortUrl).then(Mono.error(new UrlTimeoutException("Url expired!"))))
				.flatMap(e -> reportrepo.findByShortUrlAndDate(shortUrl, LocalDate.now())
						.flatMap(u -> updateHits(u))
						.switchIfEmpty(createNewUrlReport(e))
						.then(Mono.just(e.getLongUrl())));
	}

	// get all UrlReport objects created on mentioned date(GET)
	public Flux<UrlReport> getAllByDate(LocalDate date) {
		return reportrepo.findByCreateDateAndDate(date, date);
	}

	// get all UrlReport objects with hits>0 on mentioned date
	public Flux<UrlReport> getAllByHit(LocalDate date) {
		return reportrepo.findByDate(date).filter(urlReport -> urlReport.getHits() > 0);
	}

	public Flux<UrlReport> getAll() {
		return reportrepo.findAll();
	}

}
