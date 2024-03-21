package com.java.exam.service;


import com.java.exam.dto.DeliveryCostDTO;
import com.java.exam.dto.DeliveryDetailsDTO;
import com.java.exam.dto.VoucherCodeDTO;
import com.java.exam.exception.delivery.DeliveryItemException;
import com.java.exam.service.rules.RuleService;
import com.java.exam.service.voucher.VoucherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryCostService {

    private final RuleService ruleService;
    private final VoucherService voucherService;

    public DeliveryCostDTO computeDeliveryCost(DeliveryDetailsDTO deliveryDetails, VoucherCodeDTO voucherCode) {
        validateDeliveryItem(deliveryDetails);
        Double volume = computeVolume(deliveryDetails);
        log.info("VOLUME IS {} || WEIGHT IS {}", volume, deliveryDetails.getWeight());

        BigDecimal cost = ruleService.determineRuleAndGetCost(deliveryDetails.getWeight(), volume);

        if(voucherCode != VoucherCodeDTO.NONE) {
            log.info("Using a voucher");
            cost = voucherService.useVoucher(cost, voucherCode);
        }

        return computeAndReturnCost(cost);
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
            String errorMessage = "Delivery Item information has null values";
            log.warn(errorMessage);
            throw new DeliveryItemException(errorMessage);
        }
    }

    private DeliveryCostDTO computeAndReturnCost(BigDecimal cost) {
        return DeliveryCostDTO.builder()
                .cost(cost)
                .build();
    }
}
