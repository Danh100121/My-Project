package com.javamaster.project2.Repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javamaster.project2.Model.Course;

public interface CourseRepo extends JpaRepository<Course, Integer> {
    @Query("SELECT c FROM Course c WHERE c.name LIKE :x ")
    Page<Course> searchByName(@Param("x") String s,  Pageable pageable);
}