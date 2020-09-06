package com.reporting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {
	
	@GetMapping("/")
	String home() {
		log.info("home()");
		return "home";
	}
}