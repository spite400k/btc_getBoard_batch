package com.btc.get.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.btc.get.entity.TrnRateEntity;

@Repository
public interface TrnRateRepository extends JpaRepository<TrnRateEntity, Long> {

	@Query(value = "select max(auto_id) as auto_id from trn_rate order by auto_id desc", nativeQuery = true)
	Long selectMaXAutoId();
}