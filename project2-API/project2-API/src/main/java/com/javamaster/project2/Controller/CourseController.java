package com.javamaster.project2.Controller;


import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.javamaster.project2.Model.Course;
import com.javamaster.project2.Repository.CourseRepo;
import com.javamaster.project2.dto.ResponseDTO;

@RestController
@RequestMapping("/course")
public class CourseController {
 
	@Autowired
    CourseRepo courseRepo;
  
	
	  @PostMapping("/new")
	  @ResponseStatus(HttpStatus.CREATED)
	  public Course add(@ModelAttribute @Valid Course course) throws SQLException {
	        courseRepo.save(course);
	        return course;
	    }
	  
	  @GetMapping("/delete")
	    public void delete(@RequestParam("id") int id) throws SQLException {
	        courseRepo.deleteById(id);
	    }
	  
	    @PostMapping("/edit")
	    public void edit(@RequestBody @Valid Course course) throws SQLException {
	        courseRepo.save(course);
	    }
	    
	    @GetMapping("/search")
	    public ResponseDTO search(@RequestParam(name = "id", required = false) Integer id,
	                         @RequestParam(name = "name", required = false) String name,
	                         @RequestParam(name = "size", required = false) Integer size,
	                         @RequestParam(name = "page", required = false) Integer page,Model model) {
	    	ResponseDTO responseDTO = new ResponseDTO();
	    	responseDTO.setCode(200);
	        size = size == null ? 10 : size;
	        page = page == null ? 0 : page;

	        Pageable pageable =  PageRequest.of(page, size);

	        if (id != null) {
	            List<Course> courseList = courseRepo.findAllById(Arrays.asList(id));

	            responseDTO.setTotalPage(1);
	            responseDTO.setCount((long) courseList.size()) ;
	            responseDTO.setData( courseList);

	        } else {
	            Page<Course> pageCourse = null;

	            if (name != null) {
	                pageCourse = courseRepo.searchByName("%" + name + "%", pageable);
	            } else {
	                pageCourse =  courseRepo.findAll(pageable);
	            }
	            responseDTO.setTotalPage(pageCourse.getTotalPages());
	            responseDTO.setCount(pageCourse.getTotalElements());
	            responseDTO.setData(pageCourse.getContent());
	        }
	        return responseDTO ;
	    }
}
