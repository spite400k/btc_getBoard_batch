package com.btc.get.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "`trn_rate_ask`")
public class TrnRateAskEntity implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TrnRatePK trnRatePK;

	private BigDecimal price;
	private BigDecimal size;
	private Timestamp create_timestamp;
}