package com.talentica.articles.article.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(ArticleController.BASE_URL)
@RequiredArgsConstructor
@Slf4j
public class ArticleController {
	public static final String BASE_URL = "/api/article";
	
	@PostMapping(value = {"/create"}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> createArticle(@RequestBody ArticleCreateRequest articleCreateRequest){
		return ResponseEntity.ok("New article created");
	}
	
	@PutMapping(value = {"/edit/{articleId}"}, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> editArticle(@RequestBody ArticleCreateRequest articleupdateeRequest, @PathVariable Integer articleId){
		return ResponseEntity.ok(String.format("Article %s updated",articleId));
	}
	
	@GetMapping(value = "/read/{articleId}")
	public ResponseEntity<String> readArticle(@PathVariable Integer articleId){
		return ResponseEntity.ok(String.format("You reading article %s", articleId));
	}

}
