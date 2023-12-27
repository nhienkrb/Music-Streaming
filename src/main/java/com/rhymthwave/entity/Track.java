package com.rhymthwave.entity;

import java.io.Serializable;

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
@Table(name = "TRACK")
public class Track implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRACKID")
	private Long trackId;

	@Column(name = "TRACKNUMBER")
	private int trackNumber;

	@ManyToOne
	@JoinColumn(name = "ALBUMID")
	private Album album;

	@ManyToOne
	@JoinColumn(name = "RECORDINGID")
	private Recording recording;

}
