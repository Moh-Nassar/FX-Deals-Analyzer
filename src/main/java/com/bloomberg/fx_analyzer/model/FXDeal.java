package com.bloomberg.fx_analyzer.model;

import com.bloomberg.fx_analyzer.annotation.CurrencyCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("fxDeal")
public class FXDeal {
	@Id
	@NotNull(message = "Deal ID cannot be null")
	private String dealUniqueId;

	@NotNull(message = "From currency ISO code cannot be null")
	@CurrencyCode
	private String fromCurrencyIsoCode;

	@NotNull(message = "To currency ISO code cannot be null")
	@CurrencyCode
	private String toCurrencyIsoCode;

	@NotNull(message = "Deal timestamp cannot be null")
	private LocalDateTime dealTimestamp;

	@NotNull(message = "Deal amount cannot be null")
	@Min(value = 1, message = "Deal amount must be greater than 0")
	private BigDecimal dealAmount;
}
