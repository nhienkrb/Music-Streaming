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
@Table(name = "MONITOR")
public class Monitor implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MONITORID")
	private Long id;
	
	@Column(name = "DATEMONITOR")
	@Temporal(TemporalType.DATE)
	private Date dateMonitor = new Date();

	@ManyToOne
	@JoinColumn(name = "RECORDINGID")
	private Recording recording;

	@ManyToOne
	@JoinColumn(name = "ACCOUNT")
	private Account account;
}
