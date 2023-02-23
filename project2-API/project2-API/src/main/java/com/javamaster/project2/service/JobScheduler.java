//package com.javamaster.project2.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.javamaster.project2.Model.User;
//import com.javamaster.project2.Repository.UserRepo;
//
//@Component
//public class JobScheduler {
//	@Autowired
//	UserRepo userRepo;
//  @Autowired
//  MailService mailService;
//	@Scheduled(fixedDelay = 100000)
////	@Scheduled(cron="5 * * * * *")  ////  giay . phut . gio . ngay . thang . nam
//	public void sendmail() {
//		System.out.println("HELLO JOB");
//		List<User> users = userRepo.findAll();
//		for (User u : users) {
//			System.out.println(u.getName());
//		}
//		mailService.sendEmail("danh100121@gmail.com", "hello", "aa");
// }
//	
//}
