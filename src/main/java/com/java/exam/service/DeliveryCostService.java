package com.java.exam.service;


import com.java.exam.constants.DeliveryComputationProperties;
import com.java.exam.dto.DeliveryCostDTO;
import com.java.exam.dto.DeliveryDetailsDTO;
import com.java.exam.dto.DeliveryItemInformationDTO;
import com.java.exam.exception.DeliveryItemException;
import com.java.exam.service.rules.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryCostService {

    private final DeliveryComputationProperties deliveryComputationProperties;

    public DeliveryCostDTO computeDeliveryCost(DeliveryDetailsDTO deliveryDetails) {
        DeliveryItemInformationDTO deliveryItemInfo = deliveryDetails.getDeliveryItemsInfo();
        validateDeliveryItem(deliveryItemInfo);
        Double volume = computeVolume(deliveryItemInfo);
        log.info("VOLUME IS {}", volume);

        return determineRuleAndGetCost(deliveryItemInfo.getWeight(), volume);
    }

    private DeliveryCostDTO determineRuleAndGetCost(Double weight, Double volume) {
        if(weight > deliveryComputationProperties.getConditionReject()) {
            log.info("USED REJECT PARCEL LOGIC");
            throw new DeliveryItemException("Item weight is exceeding allowed weight");
        }

        if(weight > deliveryComputationProperties.getConditionHeavyParcel()) {
            log.info("USED HEAVY PARCEL LOGIC");
            return computeAndReturnCost(weight, new HeavyParcelRule(deliveryComputationProperties));
        }

        if(volume < deliveryComputationProperties.getConditionSmallParcel()) {
            log.info("USED SMALL PARCEL LOGIC");
            return computeAndReturnCost(volume, new SmallParcelRule(deliveryComputationProperties));
        }

        if(volume < deliveryComputationProperties.getConditionMediumParcel() &&
                volume > deliveryComputationProperties.getConditionSmallParcel()) {
            log.info("USED MEDIUM PARCEL LOGIC");
            return computeAndReturnCost(volume, new MediumParcelRule(deliveryComputationProperties));
        }

        log.info("USED LARGE PARCEL LOGIC");
        return computeAndReturnCost(volume, new LargeParcelRule(deliveryComputationProperties));
    }

    private DeliveryCostDTO computeAndReturnCost(Double computationBasis, DeliveryRule deliveryRule) {
        return DeliveryCostDTO.builder()
                .cost(deliveryRule.computeCost(computationBasis))
                .build();
    }

    private Double computeVolume(DeliveryItemInformationDTO deliveryItemInfo) {
        return deliveryItemInfo.getLength() *
                deliveryItemInfo.getHeight() *
                deliveryItemInfo.getWidth();
    }

    private void validateDeliveryItem(DeliveryItemInformationDTO deliveryItemInfo) {
        if(deliveryItemInfo == null ||
                deliveryItemInfo.getWeight() == null ||
                deliveryItemInfo.getHeight() == null ||
                deliveryItemInfo.getLength() == null) {
            throw new DeliveryItemException("Delivery Item information has null values");
        }
    }
}
