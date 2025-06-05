package com.techacademy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.service.ReportService;

@Controller
@RequestMapping("reports")
public class ReportController {

	private ReportService reportService = null;

	@Autowired
	public void ReporteController(ReportService reportService) {
		this.reportService = reportService;
	}
	// 日報一覧画面
		@GetMapping
		public String list(Model model) {

			model.addAttribute("listSize", reportService.findAll().size());
			model.addAttribute("employeeList", reportService.findAll());

			return "reports/list";
		}
}