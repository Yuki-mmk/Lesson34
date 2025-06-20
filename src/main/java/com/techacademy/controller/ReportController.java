package com.techacademy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.Report;
import com.techacademy.service.EmployeeService;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class ReportController {

	private ReportService reportService = null;
	private EmployeeService employeeService = null;

	@Autowired
	public ReportController(ReportService reportService, EmployeeService employeeService) {
		this.reportService = reportService;
		this.employeeService = employeeService;
	}

	// 日報一覧画面
	@GetMapping
	public String list(Model model) {

		model.addAttribute("listSize", reportService.findAll().size());
		model.addAttribute("reportList", reportService.findAll());

		return "reports/list";
	}

	// 日報詳細画面
	@GetMapping(value = "/{id}/")
	public String detail(@PathVariable("id") Integer id, Model model) {

		model.addAttribute("report", reportService.findById(id));
		return "reports/detail";
	}

	// 日報新規登録画面
	@GetMapping(value = "/add")
	public String create(@ModelAttribute Report report, Model model) {

		String code = SecurityContextHolder.getContext().getAuthentication().getName();
		String name = employeeService.findByCode(code).getName();

		model.addAttribute("employeeName", name);
		return "reports/new";
	}

	// 日報新規登録処理
	@PostMapping(value = "/add")
	public String add(@Validated Report report, BindingResult res, Model model,
			@AuthenticationPrincipal UserDetail userDetail) {

		// 入力チェック
		if (res.hasErrors()) {
			return create(report, model);
		}

		// 論理削除を行った従業員番号を指定すると例外となるためtry~catchで対応
		// (findByIdでは削除フラグがTRUEのデータが取得出来ないため)
		try {
			report.setEmployee(userDetail.getEmployee());
			ErrorKinds result = reportService.save(report);

			if (ErrorMessage.contains(result)) {
				model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
				return create(report, model);
			}

		} catch (DataIntegrityViolationException e) {
			model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DUPLICATE_EXCEPTION_ERROR),
					ErrorMessage.getErrorValue(ErrorKinds.DUPLICATE_EXCEPTION_ERROR));
			return create(report, model);
		}

		return "redirect:/reports";
	}

	// 日報削除処理
	@PostMapping(value = "/{id}/delete")
	public String delete(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetail userDetail, Model model) {

		ErrorKinds result = reportService.delete(id, userDetail);

		if (ErrorMessage.contains(result)) {
			model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
			model.addAttribute("report", reportService.findById(id));
			return detail(id, model);
		}

		return "redirect:/reports";
	}

	// 日報更新画面
	@GetMapping(value = "/{id}/update")
	public String edit(@PathVariable("id") Integer id, @ModelAttribute Report report, Model model) {
		if (id == null) {
			model.addAttribute("report", report);
		} else {
			model.addAttribute("report", reportService.findById(id));
		}
		return "reports/update";
	}

	// 日報更新処理
	@PostMapping(value = "/{id}/update")
	public String update(@PathVariable("id") Integer id, @Validated Report report, BindingResult res, Model model) {
		if (res.hasErrors()) {
			return edit(null, report, model);
		}
		try {
			ErrorKinds result = reportService.update(report, id);

			if (ErrorMessage.contains(result)) {
				model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
				return edit(null, report, model);
			}

		} catch (DataIntegrityViolationException e) {
			model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DUPLICATE_EXCEPTION_ERROR),
					ErrorMessage.getErrorValue(ErrorKinds.DUPLICATE_EXCEPTION_ERROR));
			return edit(null, report, model);
		}

		return "redirect:/reports";
	}
}