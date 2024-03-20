package com.java.exam.service.rules;


import com.java.exam.constants.DeliveryComputationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class MediumParcelRule implements DeliveryRule {

    private final DeliveryComputationProperties deliveryComputation;

    @Override
    public BigDecimal computeCost(Double computationBasis) {
        return BigDecimal.valueOf(computationBasis)
                .multiply(BigDecimal.valueOf(deliveryComputation.getCostMediumParcel()));
    }
}
