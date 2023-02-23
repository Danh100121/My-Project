package com.javamaster.project2.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Data;

@Entity
@Data
@Table(name="score")
public class Score {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Min(0)
	@Max(10)
	private Double score;// diem thi monhoc/nguoi

	@ManyToOne
	private Student student;
	@ManyToOne
	private Course course;
}