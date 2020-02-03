package com.btc.get.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LatestRateObject {

	private String product_code;
	private Timestamp timestamp;

	private Integer best_bid;
	private BigDecimal best_bid_size;
	private Integer best_ask;
	private BigDecimal best_ask_size;
	private BigDecimal total_bid_depth;
	private BigDecimal total_ask_depth;
	private Integer ltp;

	private Integer tick_id;
	private BigDecimal volume;
	private BigDecimal volume_by_product;

}