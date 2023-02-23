package com.javamaster.project2.Model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
@Table (name="student")
public class Student {
	@Id
	private Integer id;
	
	@NotBlank
	private String studentCode;

	@OneToOne
	@PrimaryKeyJoinColumn
	private User user;
}