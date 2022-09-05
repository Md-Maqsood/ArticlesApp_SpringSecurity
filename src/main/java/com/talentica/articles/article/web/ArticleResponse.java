package com.talentica.articles.article.web;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {
	private Integer id;
	private String subject;
	private String body;
	private AuthorResponse author;
	private Date createdOn; 
}
