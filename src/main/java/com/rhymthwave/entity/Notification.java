package com.rhymthwave.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "NOTIFICATION")
public class Notification implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NOTIFICATIONID")
	private Integer notificationId;
	
	@Column(name = "TITLE",columnDefinition = "nvarchar(255)")
	private String title;
	
	@Column(name = "CONTENT",columnDefinition = "varchar(max)")
	private String content;
	
	@Column(name = "active")
	private Boolean active;
	
	@Column(name = "CREATEBY",columnDefinition = "nvarchar(255)")
	private String createBy;
	
	@Column(name = "CREATEDATE")
	private Date createDate;
	
	@Column(name = "MODIFIEDBY",columnDefinition = "nvarchar(255)")
	private String modifiedBy;
	
	@Column(name = "MODIFIDATE")
	private Date modifiDate;

	@Column(name = "TO_URL" ,columnDefinition = "nvarchar(255)")
	private String toURL;

	@Column(name = "Public_IDImage" ,columnDefinition = "nvarchar(255)")
	private String publicIdImage;

	@Column(name = "URLIDImage",columnDefinition = "nvarchar(255)")
	private String urlImage;
}
