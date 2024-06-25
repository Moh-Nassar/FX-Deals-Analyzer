package com.bloomberg.fx_analyzer.repository;


import com.bloomberg.fx_analyzer.model.FXDeal;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FXDealRepository extends MongoRepository<FXDeal, Long> {
	boolean existsByDealUniqueId(String dealUniqueId);

	boolean existsByDealUniqueIdIn(Set<String> dealUniqueIds);
}

