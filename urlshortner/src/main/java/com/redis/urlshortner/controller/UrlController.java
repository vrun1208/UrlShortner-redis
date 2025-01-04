package com.redis.urlshortner.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.redis.urlshortner.exception.UrlNotFoundException;
import com.redis.urlshortner.service.UrlService;

@RestController
@RequestMapping("/url")
public class UrlController {

	private final UrlService urlService;
	
	public UrlController(UrlService urlService) {
		this.urlService = urlService;
	}
	
	@PostMapping("/shorten")
	public ResponseEntity<String> shortenUrl(@RequestBody Map<String, String> request) {
		String getUrl = request.get("url");
		if(getUrl == null || !isValidUrl(getUrl)) {
			ResponseEntity.badRequest().body("Invalid Url Format");
		}
		String shortUrl = urlService.shortenUrl(getUrl);
		return ResponseEntity.ok(shortUrl);
	}
	
	@GetMapping("/{shortUrl}")
	public RedirectView getOriginalUrl(@PathVariable String shortUrl) {
		String originalUrlString = urlService.getOriginalUrlBack(shortUrl);
			
		if(originalUrlString != null) {
			RedirectView redirectView = new RedirectView();
			redirectView.setUrl(originalUrlString);
			return redirectView;		
		}
		throw new UrlNotFoundException("Shortened URL not found.");
	}
	
	private boolean isValidUrl(String url) {
	    try {
	        new java.net.URL(url);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
}
