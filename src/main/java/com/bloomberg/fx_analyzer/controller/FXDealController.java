package com.bloomberg.fx_analyzer.controller;

import com.bloomberg.fx_analyzer.model.FXDeal;
import com.bloomberg.fx_analyzer.service.FXDealService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fxdeals")
@AllArgsConstructor
@Slf4j
public class FXDealController {

	private final FXDealService fxDealService;

	@PostMapping
	public ResponseEntity<FXDeal> createFXDeal(@Valid @RequestBody FXDeal fxDeal) {
		log.info("Processing deal with ID: {}", fxDeal.getDealUniqueId());
		FXDeal savedDeal = fxDealService.processFXDeal(fxDeal);
		log.info("FX Deal with ID: {}, was successfully processed.", fxDeal.getDealUniqueId());
		return ResponseEntity.ok(savedDeal);
	}

	@GetMapping("/all")
	public ResponseEntity<List<FXDeal>> getAllFXDeals() {
		log.info("Getting all FX Deals");
		return ResponseEntity.ok(fxDealService.getAllFXDeals());
	}

	@PostMapping("/bulk")
	public ResponseEntity<List<FXDeal>> createFXDeals(@Valid @RequestBody List<FXDeal> fxDeals) {
		log.info("Processing FX Deals in bulk...");
		List<FXDeal> savedDeals = fxDealService.processFXDealBulk(fxDeals);
		log.info("Successfully processed FX deals in bulk");
		return ResponseEntity.ok(savedDeals);
	}
}
