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
@Table(name = "TAGS")
public class Tag implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TAGID")
	private Long tagId;

	@Column(name = "NAMETAG",columnDefinition = "nvarchar(55)")
	private String tagName;
	
	@Column(name = "CREATEBY",columnDefinition = "nvarchar(255)")
	private String createBy;
	
	@Column(name = "CREATEDATE")
	private Date createDate;
	
	@Column(name = "MODIFIEDBY",columnDefinition = "nvarchar(255)")
	private String modifiedBy;
	
	@Column(name = "MODIFIDATE")
	private Date modifiDate;

	@JsonIgnore
	@OneToMany(mappedBy = "tag")
	private List<Podcast> podcasts;
}
