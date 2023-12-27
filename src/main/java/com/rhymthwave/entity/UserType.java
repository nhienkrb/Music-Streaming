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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERTYPE")
public class UserType implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USERTYPEID")
	private Integer userTypeId;

	@Column(name = "NAMETYPE",columnDefinition = "varchar(20)")
	private String nameType;

	@Column(name = "STARTDATE")
	private Date startDate;

	@Column(name = "ENDDATE")
	private Date endDate;

	@Column(name = "STATUS",columnDefinition = "nvarchar(55)")
	private String status;

	@Column(name = "PAYMENTSTATUS")
	private int paymentStatus;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "ACCOUNTID")
	private Account account;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "SUBCRIPTIONID")
	private Subscription subscription;

	@JsonIgnore
	@OneToMany(mappedBy = "usertype")
	private List<Wishlist> wishlists;

	@JsonIgnore
	@OneToMany(mappedBy = "usertype")
	private List<Report> reports;

	@JsonIgnore
	@OneToMany(mappedBy = "usertype")
	private List<Access> accesses;

	@JsonIgnore
	@OneToMany(mappedBy = "usertype")
	private List<Playlist> playlists;

}