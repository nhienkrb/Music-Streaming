package com.rhymthwave.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rhymthwave.entity.TypeEnum.ESubscription;
import com.rhymthwave.entity.TypeEnum.EUserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SUBCRIPTIONS")
public class Subscription implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SUBCRIPTIONID")
	private Integer subscriptionId;

	@Column(name = "SUBCRIPTIONTYPE",columnDefinition = "nvarchar(50)")
	private String subscriptionType;

	@Column(name = "SUBCRIPTIONCATEGORY",columnDefinition = "nvarchar(50)")
	@Enumerated(EnumType.STRING)
	private ESubscription subscriptionCategory;

	@Column(name = "PRICE",columnDefinition = "Float")
	private Float price;

	@Column(name = "DESCRIPTION",columnDefinition = "nvarchar(max)")
	private String description;

	@Column(name = "ACTIVE")
	private Boolean active;

	@Column(name = "PRDSTRIPEID")
	private String prdStripeId;

	@Column(name = "PRDPAYPALID")
	private String prdPaypalId;

	@Column(name = "CREATEDATE")
	private Date createDate;

	@Column(name = "DURATION")
	private Integer duration;

	@Column(name = "PRIORITY")
	private Integer priority;

	@Column(name = "PLAYLISTALLOW")
	private Integer playlistAllow;

	@Column(name = "NIP")
	private Integer nip;

	@JsonIgnore
	@OneToMany(mappedBy = "subscription", cascade = CascadeType.REFRESH)
	private List<UserType> userTypes;

	@JsonIgnore
	@OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
	private List<Advertisement> advertisement;
}