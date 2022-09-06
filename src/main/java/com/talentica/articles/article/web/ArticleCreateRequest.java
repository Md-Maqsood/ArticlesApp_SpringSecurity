package com.talentica.articles.article.web;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCreateRequest {
	
	@NonNull
	private String subject;
	
	private String body;
	
	@NonNull
	private Integer author;
}
