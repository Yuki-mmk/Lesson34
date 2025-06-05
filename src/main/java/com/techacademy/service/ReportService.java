package com.techacademy.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techacademy.repository.ReportRepository;

@Service
public class ReportService {

	private final ReportRepository reportRepository;
	private final PasswordEncoder passwordEncoder;

	public ReportService(ReportRepository reportRepository, PasswordEncoder passwordEncoder) {
		this.reportRepository = reportRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Object findAll() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}