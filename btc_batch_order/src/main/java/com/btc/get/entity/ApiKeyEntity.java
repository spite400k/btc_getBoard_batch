package com.btc.get.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "`api_key`")
public class ApiKeyEntity {
	@Id
	@Column(name = "api_key_cd")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String api_key_cd;

	private String api_key;
	private String api_secret;

}