package com.javamaster.project2.Controller;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.javamaster.project2.Model.UserRole;
import com.javamaster.project2.Repository.UserRepo;
import com.javamaster.project2.Repository.UserRoleRepo;
import com.javamaster.project2.dto.ResponseDTO;
//requestbody
//{
//	"user" :{
//		"id" :11
//	},
//	"role" :"ADMIN"
//}
@RestController
@RequestMapping("/userrole")
public class UserRoleController {
    @Autowired
    UserRoleRepo userRoleRepo;

    @Autowired
    UserRepo userRepo;

    @PostMapping("/new")
    public UserRole add(@ModelAttribute UserRole userRole) throws SQLException {
        userRoleRepo.save(userRole);
        return userRole;
    }
    @GetMapping("/delete")
    public void delete(@RequestParam("id") int id) throws SQLException {
        userRoleRepo.deleteById(id);
    }
    @PostMapping("/edit")
    public void edit(@RequestBody UserRole editUserRole,Model model) throws SQLException {
        UserRole current = userRoleRepo.findById(editUserRole.getId()).orElse(null);

        if (current != null) {
            // lay du lieu can update tu edit qua current, de tranh mat du lieu cu
            current.setRole(editUserRole.getRole());
            // ... set them thuoc tinh khac
            userRoleRepo.save(current);
        }
    }
    @GetMapping("/search")
    public ResponseDTO search(@RequestParam(name = "id", required = false) Integer id,
                         @RequestParam(name = "role", required = false) String role,
                         @RequestParam(name = "name", required = false) String name,
                         @RequestParam(name = "userId", required = false) Integer userId,
                         @RequestParam(name = "size", required = false) Integer size,
                         @RequestParam(name = "page", required = false) Integer page,Model model) {
    	ResponseDTO responseDTO = new ResponseDTO();
    	responseDTO.setCode(200);
    	size = size == null ? 10 : size;
        page = page == null ? 0 : page;

        Pageable pageable = PageRequest.of(page, size);

        if (id != null) {
            List<UserRole> userRoleList = userRoleRepo.findAllById(Arrays.asList(id));

            responseDTO.setTotalPage(1);
            responseDTO.setCount((long) userRoleList.size()) ;
            responseDTO.setData(userRoleList);

        } else {
            Page<UserRole> pageUserRole = null;

            if(StringUtils.hasText(name) ) {
                pageUserRole = userRoleRepo.searchByName("%" + name + "%", pageable);
            } else if (userId != null){
                pageUserRole = userRoleRepo.searchByUserId(userId, pageable);
            } else if (role != null){
                pageUserRole = userRoleRepo.searchByRole("%" + role + "%", pageable);
            } else if (StringUtils.hasText(name) && userId != null){
                pageUserRole = userRoleRepo.searchByNameAndId("%" + name + "%",userId, pageable);
            } else {
                pageUserRole = userRoleRepo.findAll(pageable);
            }
            responseDTO.setTotalPage(pageUserRole.getTotalPages());
            responseDTO.setCount(pageUserRole.getTotalElements());
            responseDTO.setData(pageUserRole.getContent());
        }
   
        model.addAttribute("userList", userRepo.findAll());
        return responseDTO;
    }
}