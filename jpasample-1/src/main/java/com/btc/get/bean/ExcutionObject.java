package com.btc.get.bean;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcutionObject {

	private BigDecimal id;
	private String side;
	private BigDecimal price;
	private BigDecimal size;
	private Date exec_date;
	private String buy_child_order_acceptance_id;
	private String sell_child_order_acceptance_id;

}