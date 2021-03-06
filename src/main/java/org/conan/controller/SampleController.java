package org.conan.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.conan.domain.SampleDTO;
import org.conan.domain.TodoDTO;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;


@RequestMapping("/sample/*") //사용자가 요청하는 ~~  http://localhost:8080/sample/ex01
@Controller //컨트롤러임을 알려줌
@Log4j    //로그를 알려줌 
public class SampleController {
	@GetMapping("/ex01")
	public String ex01(SampleDTO dto) { //dto 객체에 찍어준다 
		log.info(""+dto); // 콘솔에 찍어준다 
		return "ex01";
	}
	@GetMapping("/ex02")
	public String ex02(@RequestParam("name")String name, @RequestParam("age")int age ) {
		log.info("name  :  " + name);
		log.info("age   :  " + age );
		return "ex02";
	}
	@GetMapping("/ex02List")
	public String ex02List(@RequestParam("ids") ArrayList<String> ids) {
		log.info("ids:  "+ids);
		return "ex02List";
	}
	@GetMapping("/ex02Array")
	public String ex02Array(@RequestParam("ids") String[]ids) {
		log.info("array ids:  "+Arrays.toString(ids));
		return "ex02Array";
	}
	@GetMapping("/ex02Bean") //경로 생성 
	public String ex02Bean(SampleDTOList list) {
		log.info("list dtos : "+list);
		return "ex02Bean";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat,false));
	}
	@GetMapping("/ex03")
	public String ex03(TodoDTO todo) {
		log.info("todo : "+todo);
		return "ex03";
	}
	
	@GetMapping("/ex04") 
	public String ex04(SampleDTO dto , @ModelAttribute("page")int page) {
		log.info("dto : "+dto);
		log.info("page : "+page);
		return "/sample/ex04";
	}
	@GetMapping("/ex05")
	public void ex05() {//보이드 타입 
		log.info("ex05..............");	
	}
	@GetMapping("/ex06")
	public @ResponseBody SampleDTO ex06() { //return 타입이 @ResponseBody 제이슨으로 만들어보냄 
		
		log.info("ex06..............");
		SampleDTO dto =new SampleDTO();
		dto.setAge(10);
		dto.setName("conan");
		return dto;
	}
	
	@GetMapping("/ex07")
	public ResponseEntity <String> ex07(){
		log.info("ex07..............");
		String msg = String.format("{\"name\":\"conan\"}");
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json;charset=UTF-8");
		//header.add("Content-Type", "application/xml;charset=UTF-8"); //상태정보 
		return new ResponseEntity <>(msg,header,HttpStatus.OK);
	}
	
	@GetMapping("/exUpload")
	public void exUpload() {
		log.info("exUpload..............");
	}
	@PostMapping("/exUploadPost")
	public void exUploadPost(ArrayList<MultipartFile> files) {
		for(MultipartFile file:files) {
			log.info("name"+file.getOriginalFilename());
			log.info("size :" +file.getSize());
		}
	}
}
