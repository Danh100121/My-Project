package com.javamaster.project2.Repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javamaster.project2.Model.UserRole;

public interface UserRoleRepo extends JpaRepository<UserRole, Integer> {
    @Query("SELECT ur FROM UserRole ur WHERE ur.role LIKE :x ")
    Page<UserRole> searchByRole(@Param("x") String s, Pageable pageable);

    @Query("SELECT ur FROM UserRole ur JOIN ur.user u"
            + " WHERE u.id = :x ")
    Page<UserRole> searchByUserId(@Param("x") int userId, Pageable pageable);

    @Query("SELECT ur FROM UserRole ur JOIN ur.user u" + " WHERE u.name LIKE :x ")
    Page<UserRole> searchByName(@Param("x") String uName,Pageable pageable);

    @Query("SELECT ur FROM UserRole ur JOIN ur.user u" + " WHERE u.name LIKE :x AND u.id = :x ")
    Page<UserRole> searchByNameAndId(@Param("x") String uName,@Param("x") int id, Pageable pageable);
}