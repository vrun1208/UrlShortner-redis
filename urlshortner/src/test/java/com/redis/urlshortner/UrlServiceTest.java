package com.redis.urlshortner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.longThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.redis.urlshortner.repository.UrlRepo;
import com.redis.urlshortner.service.UrlService;

public class UrlServiceTest {

	@Mock
	private UrlRepo urlRepo;
	
	@InjectMocks
	private UrlService urlService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testShortenUrl() {
		
		String ogurlString = "https://www.google.com";
		String shorturlString = "url";
		
		String resultString = urlService.shortenUrl(ogurlString);
		
		assertNotNull(resultString);
		verify(urlRepo, times(1)).save(anyString(), eq(ogurlString), anyLong());	
	}
	
	@Test
    void testGetOriginalUrlBack_Found() {
        // Arrange
        String shortUrl = "url1";
        String originalUrl = "https://www.google.com";
        when(urlRepo.find(shortUrl)).thenReturn(originalUrl);

        // Act
        String result = urlService.getOriginalUrlBack(shortUrl);

        // Assert
        assertEquals(originalUrl, result);
        verify(urlRepo, times(1)).find(shortUrl);
    }
	
	@Test
    void testGetOriginalUrlBack_NotFound() {
        // Arrange
        String shortUrl = "url1";
        when(urlRepo.find(shortUrl)).thenReturn(null);

        // Act
        String result = urlService.getOriginalUrlBack(shortUrl);

        // Assert
        assertNull(result);
        verify(urlRepo, times(1)).find(shortUrl);
    }
}
