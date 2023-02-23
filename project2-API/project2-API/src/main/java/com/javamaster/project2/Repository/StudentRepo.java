package com.javamaster.project2.Repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javamaster.project2.Model.Student;

public interface StudentRepo extends JpaRepository<Student, Integer> {
    @Query("SELECT s FROM Student s WHERE s.studentCode LIKE :x ")
    Page<Student> searchByCode(@Param("x") String s, Pageable pageable);

    @Query("SELECT s FROM Student s JOIN s.user u WHERE u.name LIKE :x ")
    Page<Student> searchByName(@Param("x") String s, Pageable pageable);

    @Query("SELECT s FROM Student s JOIN s.user u WHERE u.id = :x ")
    Page<Student> searchByUserId(@Param("x") int userId, Pageable pageable);

    @Query("SELECT s FROM Student s JOIN s.user u WHERE u.id = :x AND s.studentCode LIKE :y ")
    Page<Student> searchByUserIdAndCode(@Param("x") int userId,@Param("y") String studentCode, Pageable pageable);

    @Query("SELECT s FROM Student s JOIN s.user u WHERE u.name LIKE :x AND s.studentCode  LIKE :y ")
    Page<Student> searchByNameAndCode(@Param("x") String s,@Param("y") String studentCode, Pageable pageable);

    @Query("SELECT s FROM Student s JOIN s.user u WHERE u.id = :x AND u.name LIKE :z AND s.studentCode  LIKE :y ")
    Page<Student> searchByUserIdAndNameAndCode(@Param("x") int userId,@Param("z") String s, @Param("y") String studentCode, Pageable pageable);
}