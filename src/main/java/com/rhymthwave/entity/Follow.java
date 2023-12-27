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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FOLLOWER")
public class Follow implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FOLLOWERID")
	private Long followerId;

	@Column(name = "FOLLOWDATE")
	private Date followDate;

	@ManyToOne
	@JoinColumn(name = "ACCOUNT_A")
	private Author authorsAccountA;

	@ManyToOne
	@JoinColumn(name = "ACCOUNT_B")
	private Author authorsAccountB;
}
