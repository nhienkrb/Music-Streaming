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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RECORDING")
public class Recording implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RECORDINGID")
	private Long recordingId;

	@Column(name = "RECORDINGNAME",columnDefinition = "nvarchar(max)")
	private String recordingName;

	@Column(name = "AUDIOFILEURL",columnDefinition = "varchar(max)")
	private String audioFileUrl;
	
	@Column(name = "PUBLICIDAUDIOFILE",columnDefinition = "varchar(max)")
	private String publicIdAudio;

	@Column(name = "LYRICSURL",columnDefinition = "nvarchar(max)")
	private String lyricsUrl;

	@Column(name = "PUBLICIDLYRICS",columnDefinition = "varchar(max)")
	private String publicIdLyrics;
	
	@Column(name = "DURATION")
	private Integer duration;

	@Column(name = "SONGSTYLE",columnDefinition = "nvarchar(55)")
	private String songStyle;

	@Column(name = "LISTENED")
	private Long listened=0L;
	
	@Column(name = "MOOD",columnDefinition = "nvarchar(55)")
	private String mood;

	@Column(name = "CULTURE",columnDefinition = "nvarchar(55)")
	private String culture;

	@Column(name = "INSTRUMENT",columnDefinition = "nvarchar(55)")
	private String instrument;

	@Column(name = "VERSIONS",columnDefinition = "nvarchar(55)")
	private String versions;

	@Column(name = "STUDIO",columnDefinition = "nvarchar(55)")
	private String studio="";

	@Column(name = "IDMV",columnDefinition = "varchar(200)")
	private String idMv="";

	@Column(name = "PRODUCE",columnDefinition = "nvarchar(55)")
	private String produce="";

	@Column(name = "RECORDINGDATE")
	private Date recordingdate = new Date();

	@Column(name = "ISDELETED")
	private Boolean isDeleted=false;
	
	@Column(name = "EMAILCREATE",columnDefinition = "varchar(255)")
	private String emailCreate;

	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne
	@JoinColumn(name = "SONGSID",nullable = true)
	private Song song;

	@JsonIgnore
	@OneToMany(mappedBy = "recording")
	private List<Monitor> monitors;
	
	@JsonIgnore
	@OneToMany(mappedBy = "recording")
	private List<Wishlist> wishlists;

	@JsonIgnore
	@OneToMany(mappedBy = "recording")
	private List<PlaylistRecord> playlistRecords;

	@JsonIgnore
	@OneToMany(mappedBy = "recording")
	private List<Report> reports;

	@JsonIgnore
	@OneToMany(mappedBy = "recording")
	private List<Track> tracks;
	
	@JsonIgnore
	@OneToMany(mappedBy = "recording",cascade = CascadeType.ALL)
	private List<SongGenre> songGenres;
	
}
