package com.javamaster.project2.Controller;


import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.javamaster.project2.dto.ResponseDTO;




@RestControllerAdvice
public class ExeptionController {
	//log : lưu thông tin lỗi ra để xem 
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	
	@ExceptionHandler({BindException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseDTO bind(BindException ex) {
		logger.error("ERROR:", ex);;
//		return ex.getFieldError().getField() + "" + ex.getFieldError().getDefaultMessage();
		String msg = ex.getFieldError().getField() + "" + ex.getFieldError().getDefaultMessage();
		return new ResponseDTO(400,msg);
	}
	@ExceptionHandler (value= {SQLException.class})
	public String sqlError(SQLException ex) {
//		ex.printStackTrace();
		logger.error("sql ex: ", ex);
		
		return "sqlError.html";//view
	}
	
}
