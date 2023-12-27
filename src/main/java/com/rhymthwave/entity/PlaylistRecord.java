package com.rhymthwave.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "PLAYLIST_RECORDING")
public class PlaylistRecord implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PLAYLIST_RECORDINGID")
	private Long playlistRecordingId;
	
	@Column(name = "DATEADDED")
	@Temporal(TemporalType.DATE)
	private Date dateAdded = new Date();

	@ManyToOne
	@JoinColumn(name = "RECORDINGID")
	private Recording recording;

	@ManyToOne
	@JoinColumn(name = "PLAYLISTSID")
	private Playlist playlist;

	@JsonIgnore
	@OneToMany(mappedBy = "playlistRecord")
	private List<Access> accesses;
}
