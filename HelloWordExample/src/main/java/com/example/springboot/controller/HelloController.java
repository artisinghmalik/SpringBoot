package com.example.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {
	
	@RequestMapping("/")
	String home(ModelMap modelMap){
		modelMap.addAttribute("Title", "My First App");
		modelMap.addAttribute("Message", "This is new");
		return "hello";
	}

}
