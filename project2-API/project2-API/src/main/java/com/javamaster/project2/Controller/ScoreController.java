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

import com.javamaster.project2.Model.Score;
import com.javamaster.project2.Repository.CourseRepo;
import com.javamaster.project2.Repository.ScoreRepo;
import com.javamaster.project2.Repository.StudentRepo;
import com.javamaster.project2.dto.ResponseDTO;

@RestController
@RequestMapping("/score")
public class ScoreController {
    @Autowired
    ScoreRepo scoreRepo;

    @Autowired
    CourseRepo courseRepo;

    @Autowired
    StudentRepo studentRepo;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Score add(@ModelAttribute @Valid Score score) throws SQLException {
        scoreRepo.save(score);
        return score;
    }
    @GetMapping("/delete")
    public void delete(@RequestParam("id") int id) throws SQLException {
        scoreRepo.deleteById(id);
    }
    @PostMapping("/edit")
    public void edit(@RequestBody Score score) throws SQLException {

        Score current = scoreRepo.findById(score.getId()).orElse(null);

        if (current != null) {
            // lay du lieu can update tu edit qua current, de tranh mat du lieu cu
            current.setScore(score.getScore());
            // ... set them thuoc tinh khac
            scoreRepo.save(current);
        }
    }
    @GetMapping("/search")
    public ResponseDTO search(@RequestParam(name = "id", required = false) Integer id,
                         @RequestParam(name = "score", required = false) Score score,
                         @RequestParam(name = "courseId", required = false) Integer courseId,
                         @RequestParam(name = "studentId", required = false) Integer studentId,
                         @RequestParam(name = "name", required = false) String  name,
                         @RequestParam(name = "studentCode", required = false) String studentCode,
                         @RequestParam(name = "size", required = false) Integer size,
                         @RequestParam(name = "page", required = false) Integer page,Model model) {
    	
    	ResponseDTO responseDTO = new ResponseDTO();
    	responseDTO.setCode(200);
        size = size == null ? 10 : size;
        page = page == null ? 0 : page;

        Pageable pageable = PageRequest.of(page, size);

        if (id != null) {
            List<Score> scoreList = scoreRepo.findAllById(Arrays.asList(id));

           responseDTO.setTotalPage(1);
           responseDTO.setCount((long) scoreList.size()) ;
           responseDTO.setData(scoreList);

        } else {
            Page<Score> pageScore = null;

            if (score != null) {
                pageScore = scoreRepo.searchByScore(score, pageable);
            } else if  (courseId != null) {
                pageScore = scoreRepo.searchByCourseId(courseId, pageable);
            } else if (studentId != null) {
                pageScore = scoreRepo.searchByStudentId(studentId, pageable);
            } else if (StringUtils.hasText(name)) {
                pageScore = scoreRepo.searchByCourseName("%" + name +"%", pageable);
            } else if (StringUtils.hasText(studentCode)) {
                pageScore = scoreRepo.searchByStudentCode("%" + name +"%", pageable);
            } else {
                pageScore = scoreRepo.findAll(pageable);
                
            }
            responseDTO.setTotalPage(pageScore.getTotalPages());
            responseDTO.setCount(pageScore.getTotalElements());
            responseDTO.setData(pageScore.getContent());
        }
       
        return responseDTO;
    }
}