package com.rhymthwave.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "ACCESS")
public class Access implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ACCESSID")
	private Integer accessId;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "PLAYLIST_RECORDINGID")
	private PlaylistRecord playlistRecord;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "USERTYPEID")
	private UserType usertype;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "EPISODESID")
	private Playlist_Podcast playlistPodcast;

}
