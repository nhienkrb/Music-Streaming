package com.rhymthwave.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SONGS")
public class Song implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SONGSID")
	private Long songId;

	@Column(name = "SONGNAME",columnDefinition = "nvarchar(55)")
	private String songName;

	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@OneToOne
	@JoinColumn(name = "IMAGEID")
	private Image image;

	@Column(name = "REALEASEDAY")
	private Date releaseDay;

	@Column(name = "ISDELETED")
	private boolean isDeleted;
	
	@Column(name = "DESCRIPTIONS",columnDefinition = "nvarchar(max)")
	private String description;
	
	@Column(name = "ARTISTCREATE")
	private Long artistCreate;

	@JsonIgnore
	@OneToMany(mappedBy = "song")
	private List<Recording> recordings;

	@JsonIgnore
	@OneToMany(mappedBy = "song",cascade = CascadeType.ALL)
	private List<Writter> writters;
	
	@PreRemove
	public void nullificarRecordings() {
		recordings.forEach(record -> record.setSong(null));
	}

}
