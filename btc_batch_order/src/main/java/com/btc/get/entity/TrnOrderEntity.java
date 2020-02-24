package com.btc.get.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "`trn_balance`")
public class TrnOrderEntity {
	@Id
	@Column(name = "currency_code")
	private String currency_code;

	private BigDecimal amount;
	private BigDecimal available;
	private Timestamp timestamp;
}