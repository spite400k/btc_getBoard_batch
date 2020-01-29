package com.btc.get.bean;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RateObject {

	private BigDecimal mid_price;

	private List<Bid> bids;

	private List<Ask> asks;

}