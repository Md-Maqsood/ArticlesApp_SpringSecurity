package com.talentica.articles.security.infra;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
public class Role {

	@Id
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name", unique = true, nullable = false)
	private String name;
}
