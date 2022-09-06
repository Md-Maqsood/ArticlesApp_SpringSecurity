package com.talentica.articles.article.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.talentica.articles.article.ArticleService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(ArticleController.BASE_URL)
@Slf4j
public class ArticleController {
	
	public static final String BASE_URL = "/api/article";
	
	@Autowired
	private ArticleService articleService;
	
	@PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ArticleResponse> createArticle(@RequestBody ArticleCreateRequest articleCreateRequest) throws Exception{
		log.info("Processing request for create article");
		return ResponseEntity.ok(articleService.createArticle(articleCreateRequest));
	}
	
	@PutMapping(value = "/edit/{articleId}", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ArticleResponse> updateArticle(@RequestBody ArticleCreateRequest articleUpdateRequest, @PathVariable Integer articleId) throws Exception{
		log.info("Processing request for edit article for article {}", articleId);
		return ResponseEntity.ok(articleService.updateArticle(articleId, articleUpdateRequest));
	}
	
	@GetMapping(value = "/read/{articleId}")
	public ResponseEntity<ArticleResponse> getArticle(@PathVariable Integer articleId) throws Exception{
		log.info("Processing request for read article for article {}", articleId);
		return ResponseEntity.ok(articleService.getArticleById(articleId));
	}
	
	@DeleteMapping(value = "delete/{articleId}")
	public ResponseEntity<Boolean> deleteArticle(@PathVariable Integer articleId) throws Exception{
		log.info("Processing request for delete article for article {}", articleId);
		return ResponseEntity.ok(articleService.deleteArticle(articleId));
	}
	
	@GetMapping(value = "/authors")
	public ResponseEntity<List<AuthorResponse>> getAllAuthors() throws Exception{
		log.info("Processing request for getting all authors");
		return ResponseEntity.ok(articleService.getAllAuthors());
	}
	
	@GetMapping(value = "/list")
	public ResponseEntity<List<ArticleResponse>> getArticlesByAuthor(@RequestParam Integer authorUserId) throws Exception{
		log.info("Processing request for getting all articles for the author");
		return ResponseEntity.ok(articleService.getArticlesByAuthor(authorUserId));
	}

}
