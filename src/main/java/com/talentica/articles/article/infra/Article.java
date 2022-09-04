package com.talentica.articles.article.infra;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.talentica.articles.security.infra.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "articles")
@AllArgsConstructor
@NoArgsConstructor
public class Article {

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "article_id", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "article_id", allocationSize = 1)
	private Integer id;
	
	@Column(name = "subject", nullable = false)
	private String subject;
	
	@Column(name = "body")
	private String body;
	
	@ManyToOne	
	@JoinColumn(name = "author")
	private User author;
	
	@Column(name = "created_on")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdOn;
}
