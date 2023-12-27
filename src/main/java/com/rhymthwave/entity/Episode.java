package com.rhymthwave.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EPISODES")
public class Episode implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EPISODESID")
	private Long episodeId;

	@Column(name = "PUBLICIDFILE",columnDefinition = "nvarchar(max)")
	private String publicIdFile;
	
	@Column(name = "FILEURL",columnDefinition = "nvarchar(max)")
	private String fileUrl;

	@Column(name = "EPISODESTITLE",columnDefinition = "nvarchar(max)")
	private String episodeTitle;

	@Column(name = "DESCRIPTIONS",columnDefinition = "nvarchar(max)")
	private String description;

	@Column(name = "PUBLISHDATE")
	private Date publishDate;

	@Column(name = "SESSIONNUMBER")
	private Integer sessionNumber;

	@Column(name = "EPNUMBER")
	private Integer episodeNumber;

	@Column(name = "EPTYPE",columnDefinition = "nvarchar(55)")
	private String episodeType;

	@Column(name = "CONTENT",columnDefinition = "nvarchar(55)")
	private String content;

	@Column(name = "ISPUBLIC")
	private boolean isPublic;
	
	@Column(name = "ISDELETED")
	private boolean isDelete;
	
	@Column(name = "LISTENED")
	private Long listened=0L;
	
	@Column(name = "DURATION")
	private Integer duration;
	
	@ManyToOne()
	@JoinColumn(name = "PODCASTID")
	@JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
	private Podcast podcast;

	@ManyToOne()
	@JoinColumn(name = "IMAGEEP")
	@JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
	private Image image;

	@JsonIgnore
	@OneToMany(mappedBy = "episode")
	private List<Playlist_Podcast> playlistPodcast;
	
	@JsonIgnore
	@OneToMany(mappedBy = "episode")
	private List<Wishlist> wishlist;

	@JsonIgnore
	@OneToMany(mappedBy = "episode")
	private List<Report> reports;
	
	@JsonIgnore
	@OneToMany(mappedBy = "episode")
	private List<MonitorEpisode> monitorEp;
}
