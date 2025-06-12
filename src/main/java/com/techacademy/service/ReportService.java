package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

@Service
public class ReportService {

	private final ReportRepository reportRepository;

	public ReportService(ReportRepository reportRepository, PasswordEncoder passwordEncoder) {
		this.reportRepository = reportRepository;
	}

	// 日報一覧表示処理
	public List<Report> findAll() {
		return reportRepository.findAll();
	}

	// 1件を検索
	public Report findById(Integer id) {
		// findByIdで検索
		Optional<Report> option = reportRepository.findById(id);
		// 取得できなかった場合はnullを返す
		Report report = option.orElse(null);
		return report;
	}

	// 日報保存
	@Transactional
	public ErrorKinds save(Report report) {

		report.setDeleteFlg(false);

		LocalDateTime now = LocalDateTime.now();
		report.setCreatedAt(now);
		report.setUpdatedAt(now);

		reportRepository.save(report);
		return ErrorKinds.SUCCESS;
	}

	// 日報更新保存
	@Transactional
	public ErrorKinds update(Report report, Integer id) {
		Report dbreport = findById(id);
		List<Report> reportList = reportRepository.findByEmployee(dbreport.getEmployee());
		for (Report rep : reportList) {
			if (rep.getReportDate().equals(report.getReportDate())) {
				if(dbreport.getReportDate().equals(report.getReportDate()))break;
				return ErrorKinds.DATECHECK_ERROR;
			}
		}

		report.setDeleteFlg(false);

		LocalDateTime now = LocalDateTime.now();
		report.setCreatedAt(now);
		report.setUpdatedAt(now);
		report.setEmployee(dbreport.getEmployee());

		reportRepository.save(report);
		return ErrorKinds.SUCCESS;
	}

	// 日報削除
	@Transactional
	public ErrorKinds delete(Integer id, UserDetail userDetail) {

		Report report = findById(id);
		LocalDateTime now = LocalDateTime.now();
		report.setUpdatedAt(now);
		report.setDeleteFlg(true);

		return ErrorKinds.SUCCESS;
	}

	public List<Report> findByEmployee(Employee employee) {
		List<Report> reportList = reportRepository.findByEmployee(employee);
		return reportList;
	}
}