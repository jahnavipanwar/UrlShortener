package com.jahnavi.Url.bean;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "UrlReport")
public class UrlReport {
	@Id
	private String id;
	private String shortUrl;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate createDate;
	private long hits;

	public UrlReport() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public UrlReport(String id, String shortUrl, LocalDate date, LocalDate createDate, long hits) {
		super();
		this.id = id;
		this.shortUrl = shortUrl;
		this.date = date;
		this.createDate = createDate;
		this.hits = hits;

	}

	public long getHits() {
		return hits;
	}

	public void setHits(long hits) {
		this.hits = hits;
	}


	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "UrlReport [id=" + id + ", shortUrl=" + shortUrl + ", date=" + date + ", createDate=" + createDate
				+ ", hits=" + hits + "]";
	}

}
