package com.btc.get.bean;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Balance {

	private String currency_code;
	private BigDecimal amount;
	private BigDecimal available;
}
