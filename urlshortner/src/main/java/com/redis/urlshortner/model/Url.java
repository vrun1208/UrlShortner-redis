package com.redis.urlshortner.model;

import lombok.Data;

@Data
public class Url {

	private String originalUrl;
	private String shortUrl;
}
