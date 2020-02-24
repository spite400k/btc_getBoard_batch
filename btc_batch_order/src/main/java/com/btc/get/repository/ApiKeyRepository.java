package com.btc.get.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.btc.get.entity.ApiKeyEntity;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, String> {

}