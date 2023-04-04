package com.jahnavi.Url.repository;

import java.util.function.Function;

//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.jahnavi.Url.bean.Url;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UrlRepository extends ReactiveMongoRepository<Url, String> {
	public Mono<Url> findByShortUrl(String shortUrl);

	public Mono<Url> findByLongUrl(String getlongUrl);

	public Mono<Url> findByUserIdAndLongUrl(String userId,String longUrl);

	public Flux<Url> findByUserId(String userId);
	
	public Mono<Long> countByUserId(String userId);
}
