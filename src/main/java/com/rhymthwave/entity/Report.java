package com.rhymthwave.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "REPORT")
public class Report implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "REPORTID")
	private Integer reportId;

	@Column(name = "REPORTDATE")
	private Date reportDate;

	@Column(name = "DESCRIPTION",columnDefinition = "nvarchar(55)")
	private String description;
	
	@Column(name = "STATUS")
	private boolean status;
	
	@ManyToOne
	@JoinColumn(name = "ARTISTID")
	private Artist artist;

	@ManyToOne
	@JoinColumn(name = "RECORDINGID")
	private Recording recording;

	@ManyToOne
	@JoinColumn(name = "POSTCASTID")
	private Podcast podcast;

	@ManyToOne
	@JoinColumn(name = "EPISODESID")
	private Episode episode;

	@ManyToOne
	@JoinColumn(name = "USERTYPEID")
	private UserType usertype;
}
