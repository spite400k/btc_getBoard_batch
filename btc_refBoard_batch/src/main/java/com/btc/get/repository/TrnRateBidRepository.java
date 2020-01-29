package com.btc.get.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.btc.get.entity.TrnRateBidEntity;

@Repository
public interface TrnRateBidRepository extends JpaRepository<TrnRateBidEntity, Long> {

}