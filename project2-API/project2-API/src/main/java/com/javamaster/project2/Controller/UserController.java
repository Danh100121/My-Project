package com.javamaster.project2.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.javamaster.project2.Model.User;
import com.javamaster.project2.Repository.UserRepo;
import com.javamaster.project2.dto.ResponseDTO;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepo userRepo;
	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;
   

    @PostMapping("/new")
    public User add(@ModelAttribute  @Valid User u, @RequestParam("file") MultipartFile file)
            throws IllegalStateException, IOException {

        if (!file.isEmpty()) {
            final String UPLOAD_FOLDER = "D:/file/";

            String filename = file.getOriginalFilename();
            File newFile = new File(UPLOAD_FOLDER + filename);

            file.transferTo(newFile);

            u.setAvatar(filename);// save to db
        }

        userRepo.save(u);
        return u;

    }
    @PostMapping("/upload-multi")
    public void add(@RequestParam("files") MultipartFile[] files) throws IllegalStateException, IOException, IOException {
        System.out.println(files.length);
        for (MultipartFile file : files)
            if (!file.isEmpty()) {
                final String UPLOAD_FOLDER = "D:/file/";

                String filename = file.getOriginalFilename();
                File newFile = new File(UPLOAD_FOLDER + filename);

                file.transferTo(newFile);
            }

     
    }
    @GetMapping("/download")
    public void download(@RequestParam("filename") String filename, HttpServletResponse response) throws IOException {
        final String UPLOAD_FOLDER = "D:/file/";

        File file = new File(UPLOAD_FOLDER + filename);

        Files.copy(file.toPath(), response.getOutputStream());
    }
    @GetMapping("/delete")
    public void delete(@RequestParam("id") int id) throws SQLException {
        userRepo.deleteById(id);
    }
    @PostMapping("/edit")
    public void edit(@RequestBody @Valid User editUser
    		        ) throws IllegalStateException, IOException {
  
        User current = userRepo.findById(editUser.getId()).orElse(null);

        if (current != null) {
            if (!editUser.getFile().isEmpty()) {
                final String UPLOAD_FOLDER = "D:/file/";

                String filename = editUser.getFile().getOriginalFilename();
                File newFile = new File(UPLOAD_FOLDER + filename);

                editUser.getFile().transferTo(newFile);

                current.setAvatar(filename);// save to db
            }

            // lay du lieu can update tu edit qua current, de tranh mat du lieu cu
            current.setName(editUser.getName());
            current.setUsername(editUser.getUsername());
            current.setPassword(editUser.getPassword());
            current.setBirthdate(editUser.getBirthdate());
            userRepo.save(current);
        }
    }
    @GetMapping("/search")
    public ResponseDTO search(@RequestParam(name = "id", required = false) Integer id,
                         @RequestParam(name = "name", required = false) String name,

                         @RequestParam(name = "start", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date start,
                         @RequestParam(name = "end", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date end,

                         @RequestParam(name = "size", required = false) Integer size,
                         @RequestParam(name = "page", required = false) Integer page, Model model) {
    	ResponseDTO responseDTO = new ResponseDTO();
    	responseDTO.setCode(200);
        size = size == null ? 10 : size;
        page = page == null ? 0 : page;

        Pageable pageable = PageRequest.of(page, size);

        if (id != null) {
            List<User> users = userRepo.findAllById(Arrays.asList(id));

            responseDTO.setTotalPage(1);
            responseDTO.setCount((long) users.size()) ;
            responseDTO.setData(users);
        } else {
            Page<User> pageUser = null;

            if (StringUtils.hasText(name) && start != null && end != null) {
                pageUser = userRepo.searchByNameAndDate("%" + name + "%", start, end, pageable);
            } else if (StringUtils.hasText(name) && start != null) {
                pageUser = userRepo.searchByNameAndStartDate("%" + name + "%", start, pageable);
            } else if (StringUtils.hasText(name) && end != null) {
                pageUser = userRepo.searchByNameAndEndDate("%" + name + "%", end, pageable);
            } else if (StringUtils.hasText(name)) {
                pageUser = userRepo.searchByName("%" + name + "%", pageable);
            } else if (start != null && end != null) {
                pageUser = userRepo.searchByDate(start, end, pageable);
            } else if (start != null) {
                pageUser = userRepo.searchByStartDate(start, pageable);
            } else if (end != null) {
                pageUser = userRepo.searchByEndDate(end, pageable);
            } else {
                pageUser = userRepo.findAll(pageable);
            }

            responseDTO.setTotalPage(pageUser.getTotalPages());
            responseDTO.setCount(pageUser.getTotalElements());
            responseDTO.setData(pageUser.getContent());
        }
        kafkaTemplate.send("notification",responseDTO);
      
        return responseDTO;
    }
}