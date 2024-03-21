package com.java.exam.service.rules;

import com.java.exam.constants.DeliveryComputationProperties;
import com.java.exam.exception.delivery.DeliveryItemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuleService {

    private final DeliveryComputationProperties deliveryComputationProperties;

    public BigDecimal determineRuleAndGetCost(Double weight, Double volume) {
        if(weight > deliveryComputationProperties.getConditionReject()) {
            String errorMessage = "Item weight is exceeding allowed weight";
            log.warn(errorMessage);
            throw new DeliveryItemException("Item weight is exceeding allowed weight");
        }

        if(weight > deliveryComputationProperties.getConditionHeavyParcel()) {
            return computeCost(weight, new HeavyParcelRule(deliveryComputationProperties));
        }

        if(volume < deliveryComputationProperties.getConditionSmallParcel()) {
            return computeCost(volume, new SmallParcelRule(deliveryComputationProperties));
        }

        if(volume < deliveryComputationProperties.getConditionMediumParcel() &&
                volume > deliveryComputationProperties.getConditionSmallParcel()) {
            return computeCost(volume, new MediumParcelRule(deliveryComputationProperties));
        }

        return computeCost(volume, new LargeParcelRule(deliveryComputationProperties));
    }

    private BigDecimal computeCost(Double computationBasis, DeliveryRule deliveryRule) {
        return deliveryRule.computeCost(computationBasis);
    }


}
