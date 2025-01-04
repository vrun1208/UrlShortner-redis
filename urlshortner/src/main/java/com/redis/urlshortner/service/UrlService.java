package com.redis.urlshortner.service;

import java.util.Base64;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.redis.urlshortner.repository.UrlRepo;

@Service
public class UrlService {
	
	@Value("${BASE.URL}")
	private String BASE_URL;
	
	@Value("${TTL.SECONDS}")
	private long TTL_SEC;

	private final UrlRepo urlRepo;
	
	private final AtomicLong counter = new AtomicLong();
	
	public UrlService(UrlRepo urlRepo) {
		this.urlRepo = urlRepo;
	}
	
	public String shortenUrl(String originalUrl) {
		String shortUrlString = Base64.getUrlEncoder().withoutPadding().encodeToString(("url" + counter.incrementAndGet()).getBytes());
		urlRepo.save(shortUrlString, originalUrl, TTL_SEC);
		return BASE_URL + "/" + shortUrlString;
	}
	
	public String getOriginalUrlBack(String shortUrl) {
		
		String shortKey = shortUrl.replace(BASE_URL + "/", "");
		String ogURlString =  urlRepo.find(shortKey);
		// System.out.println(ogURlString);
		if (ogURlString == null) {
	        System.out.println("Key not found or expired: " + shortKey);
	    }
	    return ogURlString;
	}
}
