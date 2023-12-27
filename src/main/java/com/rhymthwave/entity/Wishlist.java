package com.rhymthwave.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "WISHLIST")
public class Wishlist implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "WISHLISTID")
	private Long wishlistId;

	@Column(name = "ADDDATE")
	@Temporal(TemporalType.DATE)
	private Date addDate = new Date();

	@ManyToOne
	@JoinColumn(name = "RECORDINGID")
	private Recording recording;
	
	@ManyToOne
	@JoinColumn(name = "EPISODEID")
	private Episode episode;

	@ManyToOne
	@JoinColumn(name = "USERTYPEID")
	private UserType usertype;
	
}
