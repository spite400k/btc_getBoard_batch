package com.btc.get.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.btc.get.entity.TrnLatestRateEntity;

@Repository
public interface TrnLatestRateRepository extends JpaRepository<TrnLatestRateEntity, Long> {

}