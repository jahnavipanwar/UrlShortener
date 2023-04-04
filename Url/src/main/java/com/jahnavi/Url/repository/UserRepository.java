package com.jahnavi.Url.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.jahnavi.Url.bean.Url;
import com.jahnavi.Url.bean.User;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

	Mono<User> findByUserName(String name);

	boolean findByUserNameAndPassword(String name, String password);

}
