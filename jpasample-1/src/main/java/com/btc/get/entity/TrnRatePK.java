package com.btc.get.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TrnRatePK implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "auto_id")
	private Long autoId;

	@Column(name = "auto_id_detail")
	private Long autoIdDetail;
}