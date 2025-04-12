package com.bank.query.service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BalanceSummaryResponse {
	private BigDecimal totalBalance;
}
