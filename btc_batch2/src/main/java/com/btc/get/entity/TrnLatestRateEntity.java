package com.btc.get.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "`trn_latest_rate`")
public class TrnLatestRateEntity {
	@Id
	@Column(name = "auto_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long auto_id;

	private String product_code;
	private Timestamp timestamp;
	private Integer tick_id;
	private Integer best_bid;
	private Integer best_ask;
	private BigDecimal best_bid_size;
	private BigDecimal best_ask_size;
	private BigDecimal total_bid_depth;
	private BigDecimal total_ask_depth;
	private Integer ltp;
	private BigDecimal volume;
	private BigDecimal volume_by_product;
}