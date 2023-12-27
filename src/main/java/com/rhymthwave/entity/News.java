package com.rhymthwave.entity;

import java.io.Serializable;
import java.util.Date;

import com.rhymthwave.entity.TypeEnum.EROLE;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "NEWS")
//@NamedStoredProcedureQuery(name = "filter.News", procedureName = "sp_filter"
//			,parameters = {
//				@StoredProcedureParameter(mode = ParameterMode.IN, name = "month",type = Integer.class),
//				@StoredProcedureParameter(mode = ParameterMode.IN, name = "year",type = Integer.class),
//				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "newsId",type = Long.class),
//				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "title",type = String.class),
//				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "content",type = String.class),
//				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "publishDate",type = Date.class),
//				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "lastModified",type = Date.class),
//				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "createDate",type = Date.class),
//				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "createFor",type = EROLE.class),
//				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "modifiedBy",type = String.class),
//				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "modifiDate",type = Date.class),
//				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "image",type = Image.class),
//				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "account",type = Account.class)
//			}
//)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(
		name="filter.News",
		procedureName = "sp_filter",
		resultClasses = {Object.class},
		parameters = { @StoredProcedureParameter(name="month", type = Integer.class),
					  @StoredProcedureParameter(name="year", type = Integer.class)})
})
public class News implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDNEWS")
	private Integer newsId;

	@Column(name = "TITLE",columnDefinition = "nvarchar(255)")
	private String title;

	@Column(name = "CONTENT ",columnDefinition = "nvarchar(max)")
	private String content;

	@Column(name = "PUBLISHDATE")
	private Date publishDate;

	@Column(name = "LASTMODIFIED")
	private Date lastModified;

	@Column(name = "CREATEDATE" ,columnDefinition = "date")
	private Date createDate;
	
	@Column(name = "CREATEFOR",columnDefinition = "varchar(20)")
	@Enumerated(EnumType.STRING)
	private EROLE createFor;
	
	@Column(name = "MODIFIEDBY",columnDefinition = "nvarchar(255)")
	private String modifiedBy;
	
	@Column(name = "MODIFIDATE")
	private Date modifiDate;

	@Column(name = "TOURL",columnDefinition = "nvarchar(255)")
	private String toUrl;

	@OneToOne( cascade = CascadeType.REMOVE)
	@JoinColumn(name = "IMAGE")
	private Image image;

	@ManyToOne
	@JoinColumn(name = "EMAIL")
	private Account account;
}
