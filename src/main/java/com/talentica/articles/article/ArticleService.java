package com.talentica.articles.article;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.talentica.articles.article.infra.Article;
import com.talentica.articles.article.infra.ArticleRepository;
import com.talentica.articles.article.web.ArticleCreateRequest;
import com.talentica.articles.article.web.ArticleResponse;
import com.talentica.articles.article.web.AuthorResponse;
import com.talentica.articles.exception.ArticleNotFoundException;
import com.talentica.articles.exception.UserNotFoundException;
import com.talentica.articles.security.CustomUserDetails;
import com.talentica.articles.security.infra.Role;
import com.talentica.articles.security.infra.RoleRepository;
import com.talentica.articles.security.infra.User;
import com.talentica.articles.security.infra.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ArticleService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	public ArticleResponse createArticle(ArticleCreateRequest articleCreateRequest) throws RuntimeException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
		String roleCurrentUser = authentication.getAuthorities().stream().findFirst().orElseThrow(
				() -> new AccessDeniedException("You do not have authority to create article")
			).getAuthority(); 
		Integer userIdCurrentUser = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
		if((!(roleCurrentUser.equals("ROLE-ADMIN")) && (!userIdCurrentUser.equals(articleCreateRequest.getAuthor())))){
			new AccessDeniedException("You do not have authority to create article in name of another user");
		}
		User author = userRepository.findById(articleCreateRequest.getAuthor()).orElseThrow(
						()-> new UserNotFoundException(String.format("No author found with the id %s",articleCreateRequest.getAuthor()))
						);
		if(!author.getRole().getId().equals(2)) {
			throw new UserNotFoundException(String.format("User found with the id %s is not an author",author.getId()));
		}
		Article article = new Article();
		article.setAuthor(author);
		article.setSubject(articleCreateRequest.getSubject());
		article.setBody(articleCreateRequest.getBody());
		article.setCreatedOn(new Date());
		Article savedArticle = articleRepository.save(article);
		log.info("Created new article {}", savedArticle.getId());
		return new ArticleResponse(savedArticle.getId(),
								savedArticle.getSubject(),
								savedArticle.getBody(),
								new AuthorResponse(savedArticle.getAuthor().getId(), savedArticle.getAuthor().getUsername()),
								savedArticle.getCreatedOn()
								);
	}

	public ArticleResponse updateArticle(Integer articleId, ArticleCreateRequest articleUpdateRequest) throws RuntimeException {
		User author = userRepository.findById(articleUpdateRequest.getAuthor()).orElseThrow(
				()-> new UserNotFoundException(String.format("No author found with the id %s",articleUpdateRequest.getAuthor()))
				);
		Article article = articleRepository.findById(articleId).orElseThrow(
					() -> new ArticleNotFoundException(String.format("No article found with the id", articleId))
				);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String roleCurrentUser = authentication.getAuthorities().stream().findFirst().orElseThrow(
				() -> new AccessDeniedException("You do not have authority to edit article")
			).getAuthority(); 
		if(roleCurrentUser.equals("ROLE_ADMIN")) {
			if(!author.getRole().getId().equals(2)) {
				throw new UserNotFoundException(String.format("User found with the id %s is not an author",author.getId()));
			}
			article.setAuthor(author);
			article.setSubject(articleUpdateRequest.getSubject());
			article.setBody(articleUpdateRequest.getBody());
			Article savedArticle = articleRepository.save(article);
			log.info("Updated article {} by Admin", savedArticle.getId());
			return new ArticleResponse(savedArticle.getId(),
								savedArticle.getSubject(),
								savedArticle.getBody(),
								new AuthorResponse(savedArticle.getAuthor().getId(), savedArticle.getAuthor().getUsername()),
								savedArticle.getCreatedOn()
								);
		}
		Integer userIdCurrentUser = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
		if(userIdCurrentUser.equals(article.getAuthor().getId())) {
			article.setSubject(articleUpdateRequest.getSubject());
			article.setBody(articleUpdateRequest.getBody());
			Article savedArticle = articleRepository.save(article);
			log.info("Updated article {} by Author", savedArticle.getId());
			return new ArticleResponse(savedArticle.getId(),
								savedArticle.getSubject(),
								savedArticle.getBody(),
								new AuthorResponse(savedArticle.getAuthor().getId(), savedArticle.getAuthor().getUsername()),
								savedArticle.getCreatedOn()
								);
		}
		throw new AccessDeniedException("You do not have authority to edit the article");
	}

	public ArticleResponse getArticleById(Integer articleId) {
		Article article = articleRepository.findById(articleId).orElseThrow(
				() -> new ArticleNotFoundException(String.format("No article found with the id", articleId))
			);
		return new ArticleResponse(article.getId(),
				article.getSubject(),
				article.getBody(),
				new AuthorResponse(article.getAuthor().getId(), article.getAuthor().getUsername()),
				article.getCreatedOn()
				);
	}

	public Boolean deleteArticle(Integer articleId) {
		Article article = articleRepository.findById(articleId).orElseThrow(
				() -> new ArticleNotFoundException(String.format("No article found with the id", articleId))
			);
		articleRepository.delete(article);
		log.info("Deleted article {} by Admin", article.getId());
		return true;
	}

	public List<AuthorResponse> getAllAuthors() {
		return userRepository.findByRole(roleRepository.findById(2).orElse(new Role(2, "ROLE_AUTHOR")))
							.stream()
							.map((author) -> new AuthorResponse(author.getId(), author.getUsername()))
							.collect(Collectors.toList());
	}
	
	public List<ArticleResponse> getArticlesByAuthor(Integer authorUserId){
		User author = userRepository.findById(authorUserId).orElseThrow(
				()-> new UserNotFoundException(String.format("No author found with the id %s",authorUserId))
				);
		return articleRepository.findByAuthor(author).stream()
								.map((article) -> new ArticleResponse(article.getId(),
										article.getSubject(),
										article.getBody(),
										new AuthorResponse(article.getAuthor().getId(), article.getAuthor().getUsername()),
										article.getCreatedOn()
										)).collect(Collectors.toList());
	}
}
