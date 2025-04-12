package com.bank.query.service.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommissionReportResponse {
	private String productType;
    private BigDecimal totalCommission;
}
