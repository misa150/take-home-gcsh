package com.java.exam.service.rules;

import com.java.exam.constants.DeliveryComputationProperties;
import com.java.exam.dto.DeliveryCostDTO;
import com.java.exam.exception.DeliveryItemException;
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
            log.info("USED REJECT PARCEL LOGIC");
            throw new DeliveryItemException("Item weight is exceeding allowed weight");
        }

        if(weight > deliveryComputationProperties.getConditionHeavyParcel()) {
            log.info("USED HEAVY PARCEL LOGIC");
            return computeCost(weight, new HeavyParcelRule(deliveryComputationProperties));
        }

        if(volume < deliveryComputationProperties.getConditionSmallParcel()) {
            log.info("USED SMALL PARCEL LOGIC");
            return computeCost(volume, new SmallParcelRule(deliveryComputationProperties));
        }

        if(volume < deliveryComputationProperties.getConditionMediumParcel() &&
                volume > deliveryComputationProperties.getConditionSmallParcel()) {
            log.info("USED MEDIUM PARCEL LOGIC");
            return computeCost(volume, new MediumParcelRule(deliveryComputationProperties));
        }

        log.info("USED LARGE PARCEL LOGIC");
        return computeCost(volume, new LargeParcelRule(deliveryComputationProperties));
    }

    private BigDecimal computeCost(Double computationBasis, DeliveryRule deliveryRule) {
        return deliveryRule.computeCost(computationBasis);
    }


}
