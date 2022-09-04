package com.talentica.articles.article.web;

import org.springframework.lang.NonNull;

import lombok.Data;

@Data
public class ArticleCreateRequest {
	
	private String subject;
	
	private String body;
	
	@NonNull
	private Integer author;
}
