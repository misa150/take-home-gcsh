package com.java.exam.service.rules;

import java.math.BigDecimal;

public interface DeliveryRule {
    BigDecimal computeCost(Double computationBasis);
}
