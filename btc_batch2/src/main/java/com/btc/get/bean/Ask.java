package com.btc.get.bean;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ask {
	private BigDecimal price;

	private BigDecimal size;

}
