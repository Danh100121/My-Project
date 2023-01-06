package com.javamaster.project2.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javamaster.project2.Entity.Billitem;





public interface BillitemRepo extends JpaRepository<Billitem, Integer>{
//	 @Query("SELECT bi FROM Billitem bi JOIN bi.user u WHERE u.name LIKE :x")
//	  Page<Billitem> searchByNameUser(@Param("x") String name ,Pageable pageable);   

}
