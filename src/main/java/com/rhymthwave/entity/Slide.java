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
@Table(name = "SLIDE")
public class Slide implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SLIDEID")
	private Integer slideId;

	@Column(name = "POSITION",columnDefinition = "nvarchar(55)")
	private String position;

	@Column(name = "LISTIMAGE",columnDefinition = "nvarchar(max)")
	private String listImage;
	
	@Column(name = "CREATEBY",columnDefinition = "nvarchar(255)")
	private String createBy;
	
	@Column(name = "CREATEDATE")
	private Date createDate;
	
	@Column(name = "MODIFIEDBY",columnDefinition = "nvarchar(255)")
	private String modifiedBy;
	
	@Column(name = "MODIFIDATE")
	private Date modifiDate;
	
	@Column(name = "URL",columnDefinition = "varchar(max)")
	private String url;
}
