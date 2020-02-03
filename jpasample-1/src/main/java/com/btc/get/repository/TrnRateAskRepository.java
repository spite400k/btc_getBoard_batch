package com.btc.get.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.btc.get.entity.TrnRateAskEntity;

@Repository
public interface TrnRateAskRepository extends JpaRepository<TrnRateAskEntity, Long> {

}