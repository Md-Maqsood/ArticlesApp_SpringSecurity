/**
 * 
 */
package com.talentica.articles.exception;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MaqsoodA
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
	private Integer status;
	private String message;
	private Map<String, Object> details;
}
