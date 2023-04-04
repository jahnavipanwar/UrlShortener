package com.jahnavi.Url.bean;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.Id;
//import jakarta.persistence.Lob;

//Stored in DB
@Document(collection = "Url")
public class Url {
	@Id
	private String id;
//	List<User> users;
	private String userId;
	private String longUrl;
	private String shortUrl;
	private LocalDateTime creation;
	private LocalDateTime expiration;
	
	public Url() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Url(String id, String userId, String longUrl, String shortUrl, LocalDateTime creation,
			LocalDateTime expiration) {
		super();
		this.id = id;
		this.userId = userId;
		this.longUrl = longUrl;
		this.shortUrl = shortUrl;
		this.creation = creation;
		this.expiration = expiration;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getCreation() {
		return creation;
	}

	public void setCreation(LocalDateTime creation) {
		this.creation = creation;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public LocalDateTime getExpiration() {
		return expiration;
	}

	public void setExpiration(LocalDateTime expiration) {
		this.expiration = expiration;
	}

	@Override
	public String toString() {
		return "Url [id=" + id + ", userId=" + userId + ", longUrl=" + longUrl + ", shortUrl=" + shortUrl
				+ ", creation=" + creation + ", expiration=" + expiration + "]";
	}	
}
