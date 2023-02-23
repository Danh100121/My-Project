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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.javamaster.project2.Model.Student;
import com.javamaster.project2.Repository.StudentRepo;
import com.javamaster.project2.Repository.UserRepo;
import com.javamaster.project2.dto.ResponseDTO;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepo studentRepo;

    @Autowired
    UserRepo userRepo ;

    //jackson
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Student add(@ModelAttribute @Valid Student student) throws SQLException {
        studentRepo.save(student);
        return student;
    }
    @GetMapping("/delete")
    public void delete(@RequestParam("id") int id) throws SQLException {
        studentRepo.deleteById(id);
    }
    @PostMapping("/edit")
    public void edit(@RequestBody @Valid Student student) throws SQLException {
        studentRepo.save(student);
    }
    @GetMapping("/search")
    public ResponseDTO search(@RequestParam(name = "id", required = false) Integer id,
                         @RequestParam(name = "name", required = false) String name,
                         @RequestParam(name = "studentCode", required = false) String studentCode,
                         @RequestParam(name = "userId", required = false) Integer userId,
                         @RequestParam(name = "size", required = false) Integer size,
                         @RequestParam(name = "page", required = false) Integer page,Model model) {
    	
    	ResponseDTO responseDTO = new ResponseDTO();
    	responseDTO.setCode(200);
        size = size == null ? 10 : size;
        page = page == null ? 0 : page;

        Pageable pageable = PageRequest.of(page, size);

        if (id != null) {
            List<Student> studentList = studentRepo.findAllById(Arrays.asList(id));
            responseDTO.setTotalPage(1);
            responseDTO.setCount((long) studentList.size()) ;
            responseDTO.setData(studentList);
            

        } else {
            Page<Student> pageStudent = null;

           if (StringUtils.hasText(name) && userId != null && studentCode !=null) {
                pageStudent = studentRepo.searchByUserIdAndNameAndCode(userId,"%" + name + "%","%" + studentCode + "%", pageable);
            } else if (StringUtils.hasText(name) && studentCode !=null){
                pageStudent = studentRepo.searchByNameAndCode("%" + name + "%","%" + studentCode + "%", pageable);
            } else if (userId != null && studentCode !=null) {
                pageStudent = studentRepo.searchByUserIdAndCode(userId, "%" + studentCode + "%", pageable);
            } else
            if (userId != null ) {
                pageStudent = studentRepo.searchByUserId(userId, pageable);
            } else if (studentCode !=null) {
                pageStudent = studentRepo.searchByCode("%" + studentCode + "%", pageable);
            } else if (StringUtils.hasText(name)) {
                pageStudent = studentRepo.searchByName( "%" + name + "%", pageable);
            } else  {
                pageStudent = studentRepo.findAll(pageable);
            }
           responseDTO.setTotalPage(pageStudent.getTotalPages());
           responseDTO.setCount(pageStudent.getTotalElements());
           responseDTO.setData(pageStudent.getContent());
        }
    
        return responseDTO;
   }
}