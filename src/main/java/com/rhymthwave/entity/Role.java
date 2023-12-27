package com.rhymthwave.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rhymthwave.entity.TypeEnum.EROLE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "ROLE")

public class Role implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDROLE")
	private Integer roleId;

	@Column(name = "NAMEROLE",columnDefinition = "varchar(35)")
	@Enumerated(EnumType.STRING)
	private EROLE role = EROLE.USER;

	@JsonIgnore
	@OneToMany(mappedBy = "role")
	private List<Author> authors;
}
