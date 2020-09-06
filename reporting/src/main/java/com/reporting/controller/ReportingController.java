package com.reporting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.reporting.dto.GAVO;
import com.reporting.service.ReportingService;

@Controller
@RequestMapping("/report")
public class ReportingController {

	@Autowired
	private ReportingService reportingService;
	
	@GetMapping("/get")
	String getResponse(Model model) throws Exception {
		
		GetReportsResponse response = reportingService.getReport();
		GAVO gavo = reportingService.getVO(response);
		model.addAttribute("response", gavo);
		
		return "report";
	}
}
