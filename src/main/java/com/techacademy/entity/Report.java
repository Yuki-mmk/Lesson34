
package com.techacademy.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

public class Report {
	@Data
	@Entity
	@Table(name = "reports")
	@SQLRestriction("delete_flg = false")
	public class report {

		public static enum Role {
			GENERAL("一般"), ADMIN("管理者");

			private String name;

			private Role(String name) {
				this.name = name;
			}

			public String getValue() {
				return this.name;
			}
		}

		// ID
		@Column(length = 10)
		private Integer id;

		// 日付
		@Column(nullable = false)
		private LocalDate reportDate;

		// タイトル
		@Column(length = 100, nullable = false)
		private String title;

		// 内容
		@Column(columnDefinition = "LONGTEXT", nullable = false)
		private String content;

		// 社員番号
		@Column(length = 10, nullable = false)
		private String employeeCade;

		// 削除フラグ(論理削除を行うため)
		@Column(columnDefinition = "TINYINT", nullable = false)
		private boolean deleteFlg;

		// 登録日時
		@Column(nullable = false)
		private LocalDateTime createdAt;

		// 更新日時
		@Column(nullable = false)
		private LocalDateTime updatedAt;

		@ManyToOne
		@JoinColumn(name = "employee_code", referencedColumnName = "code", nullable = false)
		private Employee employee;

	}

}
