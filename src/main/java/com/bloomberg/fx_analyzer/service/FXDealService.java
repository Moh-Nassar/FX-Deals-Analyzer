package com.bloomberg.fx_analyzer.service;


import com.bloomberg.fx_analyzer.exception.DuplicateDealException;
import com.bloomberg.fx_analyzer.model.FXDeal;
import com.bloomberg.fx_analyzer.repository.FXDealRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class FXDealService {


	private FXDealRepository fxDealRepository;

	public FXDeal processFXDeal(FXDeal fxDeal) {
		validateDealExists(fxDeal);
		return fxDealRepository.save(fxDeal);
	}

	public List<FXDeal> processFXDealBulk(List<FXDeal> fxDeals) {
		fxDeals.forEach(this::processFXDeal);
		validateDealExists(fxDeals);
		return fxDealRepository.saveAll(fxDeals);
	}

	public List<FXDeal> getAllFXDeals() {
		return fxDealRepository.findAll();
	}

	private void validateDealExists(List<FXDeal> deals) {
		Set<String> uniqueIds = new HashSet<>();

		if (fxDealRepository.existsByDealUniqueIdIn(uniqueIds)) {
			throw new DuplicateDealException("One or more deals has already been processed.");
		}

	}

	private void validateDealExists(FXDeal fxDeal) {
		if (fxDealRepository.existsByDealUniqueId(fxDeal.getDealUniqueId())) {
			throw new DuplicateDealException("Deal with ID " + fxDeal.getDealUniqueId() + " is already processed.");
		}
	}
}
