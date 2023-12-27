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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PLAYLISTS")
public class Playlist implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PLAYLISTID")
	private Long playlistId;

	@Column(name = "PLAYLISTNAME",columnDefinition = "nvarchar(55)")
	private String playlistName;

	@Column(name = "QUANTITY")
	private int quantity=0;

	@Column(name = "ISPUBLIC")
	private Boolean isPublic;

	@Column(name = "DESCRIPTION",columnDefinition = "nvarchar(255)")
	private String description;
	
	
	@Column(name = "CREATEDATE")
	@Temporal(TemporalType.DATE)
	private Date createDate = new Date();

	@ManyToOne
	@JoinColumn(name = "USERTYPEID")
	private UserType usertype;

	@OneToOne
	@JoinColumn(name = "IMAGE")
	private Image image;

	@JsonIgnore
	@OneToMany(mappedBy = "playlist",cascade = CascadeType.ALL)
	private List<PlaylistRecord> playlistRecords;

	@JsonIgnore
	@OneToMany(mappedBy = "playlist",cascade = CascadeType.ALL)
	private List<Playlist_Podcast> playlistPodcast;
}
