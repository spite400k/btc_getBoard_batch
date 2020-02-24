package com.btc.get.bean;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

	private String product_code;
	private String child_order_type;
	private String side;
	private BigDecimal price;
	private BigDecimal size;
	private Integer minute_to_expire;
	private String time_in_force;
}
