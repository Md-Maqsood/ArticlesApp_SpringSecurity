package com.talentica.articles.article.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(ArticleController.BASE_URL)
@RequiredArgsConstructor
@Slf4j
public class ArticleController {
	public static final String BASE_URL = "/api";
	
	@GetMapping(value = {"/", "/home"})
	public ResponseEntity<String> welcomePage(){
		return ResponseEntity.ok("Welcome to Articles App");
	}

}
