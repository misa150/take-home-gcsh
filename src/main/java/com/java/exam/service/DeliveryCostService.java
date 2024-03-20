package com.java.exam.service;


import com.java.exam.constants.DeliveryComputationProperties;
import com.java.exam.dto.DeliveryCostDTO;
import com.java.exam.dto.DeliveryDetailsDTO;
import com.java.exam.dto.VoucherCodeDTO;
import com.java.exam.exception.DeliveryItemException;
import com.java.exam.service.rules.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryCostService {

    private final RuleService ruleService;

    public DeliveryCostDTO computeDeliveryCost(DeliveryDetailsDTO deliveryDetails, VoucherCodeDTO voucherCod) {
        validateDeliveryItem(deliveryDetails);
        Double volume = computeVolume(deliveryDetails);
        log.info("VOLUME IS {} || WEIGHT IS {}", volume, deliveryDetails.getWeight());

        return computeAndReturnCost(deliveryDetails.getWeight(), volume);
    }

    private Double computeVolume(DeliveryDetailsDTO deliveryItemInfo) {
        return deliveryItemInfo.getLength() *
                deliveryItemInfo.getHeight() *
                deliveryItemInfo.getWidth();
    }

    private void validateDeliveryItem(DeliveryDetailsDTO deliveryItemInfo) {
        if(deliveryItemInfo == null ||
                deliveryItemInfo.getWeight() == null ||
                deliveryItemInfo.getHeight() == null ||
                deliveryItemInfo.getLength() == null) {
            throw new DeliveryItemException("Delivery Item information has null values");
        }
    }

    private DeliveryCostDTO computeAndReturnCost(Double weight, Double volume) {
        return DeliveryCostDTO.builder()
                .cost(ruleService.determineRuleAndGetCost(weight, volume))
                .build();
    }
}
