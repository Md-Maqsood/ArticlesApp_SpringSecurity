package com.talentica.articles.article.infra;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentica.articles.security.infra.User;

public interface ArticleRepository extends JpaRepository<Article, Integer>{

	List<Article> findByAuthor(User author);
}
