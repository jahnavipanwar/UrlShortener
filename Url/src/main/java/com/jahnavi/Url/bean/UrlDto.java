package com.jahnavi.Url.bean;

//User gives this via Post Mapping
public class UrlDto {
	private String longUrl;
	private String userId;

	public UrlDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UrlDto(String longUrl, String userId) {
	super();
	this.longUrl = longUrl;
	this.userId = userId;
}
	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "UrlDto [longUrl=" + longUrl + ", userId=" + userId + "]";
	}

}
