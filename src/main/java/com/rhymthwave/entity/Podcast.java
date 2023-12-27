package com.rhymthwave.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PODCAST")
public class Podcast implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PODCASTID")
	private Integer podcastId;

	@Column(name = "PODCASTNAME",columnDefinition = "nvarchar(55)")
	private String podcastName;

	@Column(name = "BIO",columnDefinition = "nvarchar(max)")
	private String bio;

	@Column(name = "SOCIALMEDIALINK",columnDefinition = "varchar(max)")
	private String socialMediaLink;

	@Column(name = "RELEASEDATE")
	@Temporal(TemporalType.DATE)
	private Date releaseDate = new Date();

	@Column(name = "LANGUAGE",columnDefinition = "nvarchar(55)")
	private String language;
	
	@Column(name = "RATE")
	private Integer rate;

	@Column(name = "AUTHORNAME",columnDefinition = "nvarchar(100)")
	private String authorName;
	
	@OneToOne
	@JoinColumn(name = "IMGAGEID")
	private Image image;

	@ManyToOne
	@JoinColumn(name = "CATEGORY")
	private Tag tag;

	@ManyToOne
	@JoinColumn(name = "ACCOUNTID")
	private Account account;

	@JsonIgnore
	@OneToMany(mappedBy = "podcast")
	private List<Episode> Episodes;

	@JsonIgnore
	@OneToMany(mappedBy = "podcast")
	private List<Report> reports;
}
