package com.javamaster.project2.Repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javamaster.project2.Model.Score;

public interface ScoreRepo extends JpaRepository<Score, Integer> {
    @Query("SELECT sc FROM Score sc WHERE sc.score LIKE :x ")
    Page<Score> searchByScore(@Param("x") Score score, Pageable pageable);
    @Query("SELECT sc FROM Score sc WHERE sc.score = :x ")
    Page<Score> searchByScore(@Param("x") Double s, Pageable pageable);

    @Query("SELECT sc FROM Score sc JOIN sc.course c WHERE c.id = :x ")
    Page<Score> searchByCourseId(@Param("x") int cId, Pageable pageable);

    @Query("SELECT sc FROM Score sc JOIN sc.student s WHERE s.id = :x ")
    Page<Score> searchByStudentId(@Param("x") int sId, Pageable pageable);

    @Query("SELECT sc FROM Score sc JOIN sc.course c WHERE c.name LIKE :x ")
    Page<Score> searchByCourseName(@Param("x") String s, Pageable pageable);

    @Query("SELECT sc FROM Score sc JOIN sc.student s WHERE s.studentCode LIKE :x ")
    Page<Score> searchByStudentCode(@Param("x") String s, Pageable pageable);
}