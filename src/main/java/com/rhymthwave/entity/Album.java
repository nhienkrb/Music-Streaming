package com.rhymthwave.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ALBUM")
public class Album implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ALBUMID")
	private Integer albumId;

	@Column(name = "ALBUMNAME",columnDefinition = "nvarchar(55)")
	private String albumName;

	@Column(name = "RELEASEDATE")
	private Date releaseDate;
	
	@Column(name = "DESCRIPTIONS",columnDefinition = "nvarchar(max)")
	private String description;

	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@OneToOne
	@JoinColumn(name = "COVERIMAGE")
	private Image image;

	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@OneToOne
	@JoinColumn(name = "ARTISTID")
	private Artist artist;

	
	@JsonIgnore
	@OneToMany(mappedBy = "album",cascade = CascadeType.ALL)
	private List<Track> tracks;

}
