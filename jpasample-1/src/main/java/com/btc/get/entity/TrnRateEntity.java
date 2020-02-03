package com.btc.get.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "`trn_rate`")
public class TrnRateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long auto_id;

	private BigDecimal mid_price;
	private Timestamp create_timestamp;
}