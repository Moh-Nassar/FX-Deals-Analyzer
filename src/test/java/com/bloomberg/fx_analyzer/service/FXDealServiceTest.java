package com.bloomberg.fx_analyzer.service;

import com.bloomberg.fx_analyzer.exception.DuplicateDealException;
import com.bloomberg.fx_analyzer.model.FXDeal;
import com.bloomberg.fx_analyzer.repository.FXDealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FXDealServiceTest {

	@Mock
	private FXDealRepository fxDealRepository;

	@InjectMocks
	private FXDealService fxDealService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testProcessFXDeal_NewDeal_Success() {

		FXDeal fxDeal = new FXDeal("12345", "USD", "EUR", LocalDateTime.now(), BigDecimal.valueOf(1000));
		when(fxDealRepository.existsByDealUniqueId(fxDeal.getDealUniqueId())).thenReturn(false);
		when(fxDealRepository.save(fxDeal)).thenReturn(fxDeal);

		FXDeal savedDeal = fxDealService.processFXDeal(fxDeal);

		assertNotNull(savedDeal);
		assertEquals(fxDeal.getDealUniqueId(), savedDeal.getDealUniqueId());
		assertEquals(fxDeal.getFromCurrencyIsoCode(), savedDeal.getFromCurrencyIsoCode());
		assertEquals(fxDeal.getToCurrencyIsoCode(), savedDeal.getToCurrencyIsoCode());
		assertEquals(fxDeal.getDealAmount(), savedDeal.getDealAmount());
		verify(fxDealRepository, times(1)).existsByDealUniqueId(fxDeal.getDealUniqueId());
		verify(fxDealRepository, times(1)).save(fxDeal);
	}

	@Test
	void testProcessFXDeal_DuplicateDeal_ExceptionThrown() {
		FXDeal fxDeal = new FXDeal("12345", "USD", "EUR", LocalDateTime.now(), BigDecimal.valueOf(1000));
		when(fxDealRepository.existsByDealUniqueId(fxDeal.getDealUniqueId())).thenReturn(true);

		DuplicateDealException exception = assertThrows(DuplicateDealException.class,
				() -> fxDealService.processFXDeal(fxDeal));
		assertEquals("Deal with ID " + fxDeal.getDealUniqueId() + " is already processed.", exception.getMessage());

		verify(fxDealRepository, times(1)).existsByDealUniqueId(fxDeal.getDealUniqueId());
		verify(fxDealRepository, never()).save(any());
	}

	@Test
	void testProcessFXDealBulk_Success() {
		List<FXDeal> fxDeals = Arrays.asList(
				new FXDeal("12345", "USD", "EUR", LocalDateTime.now(), BigDecimal.valueOf(1000)),
				new FXDeal("67890", "GBP", "JPY", LocalDateTime.now(), BigDecimal.valueOf(5000))
		);
		when(fxDealRepository.existsByDealUniqueId(anyString())).thenReturn(false);
		when(fxDealRepository.saveAll(fxDeals)).thenReturn(fxDeals);

		List<FXDeal> savedDeals = fxDealService.processFXDealBulk(fxDeals);

		assertNotNull(savedDeals);
		assertEquals(2, savedDeals.size());
		assertEquals(fxDeals.get(0).getDealUniqueId(), savedDeals.get(0).getDealUniqueId());
		assertEquals(fxDeals.get(1).getDealUniqueId(), savedDeals.get(1).getDealUniqueId());
		verify(fxDealRepository, times(2)).existsByDealUniqueId(anyString());
		verify(fxDealRepository, times(1)).saveAll(fxDeals);
	}

	@Test
	void testGetAllFXDeals_Success() {
		List<FXDeal> fxDeals = Arrays.asList(
				new FXDeal("12345", "USD", "EUR", LocalDateTime.now(), BigDecimal.valueOf(1000)),
				new FXDeal("67890", "GBP", "JPY", LocalDateTime.now(), BigDecimal.valueOf(5000))
		);
		when(fxDealRepository.findAll()).thenReturn(fxDeals);

		List<FXDeal> retrievedDeals = fxDealService.getAllFXDeals();

		assertNotNull(retrievedDeals);
		assertEquals(2, retrievedDeals.size());
		assertEquals(fxDeals.get(0).getDealUniqueId(), retrievedDeals.get(0).getDealUniqueId());
		assertEquals(fxDeals.get(1).getDealUniqueId(), retrievedDeals.get(1).getDealUniqueId());
		verify(fxDealRepository, times(1)).findAll();
	}

	@Test
	void testValidateDealExists_SingleDeal_Duplicate() {
		FXDeal fxDeal = new FXDeal("12345", "USD", "EUR", LocalDateTime.now(), BigDecimal.valueOf(1000));
		when(fxDealRepository.existsByDealUniqueId(fxDeal.getDealUniqueId())).thenReturn(true);

		assertThrows(DuplicateDealException.class, () -> fxDealService.processFXDeal(fxDeal));
		verify(fxDealRepository, times(1)).existsByDealUniqueId(fxDeal.getDealUniqueId());
		verify(fxDealRepository, never()).save(any());
	}

	@Test
	void testValidateDealExists_SingleDeal_NotDuplicate() {
		FXDeal fxDeal = new FXDeal("12345", "USD", "EUR", LocalDateTime.now(), BigDecimal.valueOf(1000));
		when(fxDealRepository.existsByDealUniqueId(fxDeal.getDealUniqueId())).thenReturn(false);
		when(fxDealRepository.save(fxDeal)).thenReturn(fxDeal);

		FXDeal savedDeal = fxDealService.processFXDeal(fxDeal);

		verify(fxDealRepository, times(1)).existsByDealUniqueId(fxDeal.getDealUniqueId());
		verify(fxDealRepository, times(1)).save(fxDeal);
	}

	@Test
	void testValidateDealExists_BulkDeals_Duplicate() {
		List<FXDeal> fxDeals = Arrays.asList(
				new FXDeal("12345", "USD", "EUR", LocalDateTime.now(), BigDecimal.valueOf(1000)),
				new FXDeal("67890", "GBP", "JPY", LocalDateTime.now(), BigDecimal.valueOf(5000))
		);
		when(fxDealRepository.existsByDealUniqueIdIn(any())).thenReturn(true);

		assertThrows(DuplicateDealException.class, () -> fxDealService.processFXDealBulk(fxDeals));
		verify(fxDealRepository, times(1)).existsByDealUniqueIdIn(any());
		verify(fxDealRepository, never()).saveAll(any());
	}

	@Test
	void testValidateDealExists_BulkDeals_NotDuplicate() {
		List<FXDeal> fxDeals = Arrays.asList(
				new FXDeal("12345", "USD", "EUR", LocalDateTime.now(), BigDecimal.valueOf(1000)),
				new FXDeal("67890", "GBP", "JPY", LocalDateTime.now(), BigDecimal.valueOf(5000))
		);
		when(fxDealRepository.existsByDealUniqueIdIn(any())).thenReturn(false);
		when(fxDealRepository.saveAll(fxDeals)).thenReturn(fxDeals);

		List<FXDeal> savedDeals = fxDealService.processFXDealBulk(fxDeals);

		verify(fxDealRepository, times(1)).existsByDealUniqueIdIn(any());
		verify(fxDealRepository, times(1)).saveAll(fxDeals);
	}
}
