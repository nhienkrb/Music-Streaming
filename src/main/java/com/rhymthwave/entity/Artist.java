package com.rhymthwave.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Nationalized;

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
@Table(name = "ARTIST")
public class Artist implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ARTISTID")
	private Long artistId;

	@Column(name = "ARTISTNAME",columnDefinition = "nvarchar(55)")
	private String artistName;

	@Column(name = "DATEOFBIRTH")
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;

	@Column(name = "FULLNAME",columnDefinition = "nvarchar(55)")
	private String fullName;

	@Column(name = "PLACEOFBIRTH",columnDefinition = "nvarchar(100)")
	private String placeOfBirth;

	@Column(name = "BIO",columnDefinition = "nvarchar(max)")
	private String bio;

	@Column(name = "IMAGEGALLERY",columnDefinition = "nvarchar(max)")
	private String[] imagesGallery;
	
	@Column(name = "PUBLICIDIMAGEGALLERY",columnDefinition = "nvarchar(max)")
	private String[] publicIdImageGallery;

	@Column(name = "SOCIALMEDIALINKS",columnDefinition = "nvarchar(max)")
	private String[] socialMediaLinks;

	@Column(name = "ACTIVE")
	private Boolean active=false;

	@Column(name = "VERIFY")
	private Boolean isVerify;
	
	@Column(name="EXPIREPERMISSION")
	@Temporal(TemporalType.DATE)
	private Date expirePermission;

	@Column(name = "DATESTARTED")
	@Temporal(TemporalType.DATE)
	private Date dateStarted = new Date();

	@OneToOne
	@JoinColumn(name = "PROFILEIMAGE")
	private Image imagesProfile;

	@OneToOne
	@JoinColumn(name = "BACKGROUDIMAGE")
	private Image backgroundImage;

	@OneToOne
	@JoinColumn(name = "EMAIL")
	private Account account;

	@JsonIgnore
	@OneToMany(mappedBy = "artist")
	private List<Report> reports;

	@JsonIgnore
	@OneToMany(mappedBy = "artist")
	private List<Album> albums;

	@JsonIgnore
	@OneToMany(mappedBy = "artist")
	private List<Writter> writters;
}
