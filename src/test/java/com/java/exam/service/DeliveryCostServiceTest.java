package com.java.exam.service;

import com.java.exam.constants.DeliveryComputationProperties;
import com.java.exam.dto.DeliveryDetailsDTO;
import com.java.exam.dto.VoucherCodeDTO;
import com.java.exam.exception.DeliveryItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryCostServiceTest {

    public static final Double COST_MULTIPLIER = Double.valueOf(1);

    public static final Double REJECT_WEIGHT_CONDITION_IN_KG = Double.valueOf(50);
    public static final Double LARGE_WEIGHT_CONDITION_IN_KG = Double.valueOf(10);
    public static final Double SMALL_VOLUME_CONDITION_IN_CM3 = Double.valueOf(1500);
    public static final Double MEDIUM_VOLUME_CONDITION_IN_CM3 = Double.valueOf(2500);

    private DeliveryCostService cut;

    @Mock
    private DeliveryComputationProperties deliveryComputationProperties;

    @BeforeEach
    private void setup() {
        cut = new DeliveryCostService(deliveryComputationProperties);
    }

    @Test
    void computeDeliveryCost_inValidDeliveryDetails_throwValidationError() {
        // when
        final DeliveryItemException exception = assertThrows(DeliveryItemException.class,
                ()-> cut.computeDeliveryCost(null, VoucherCodeDTO.MYNT));

        // then
        assertEquals("Delivery Item information has null values", exception.getMessage());
    }

    @Test
    void computeDeliveryCost_validDeliveryDetails_triggerRejectRule() {
        // given
        var deliveryDetails = createDeliveryDetailsDTO(Double.valueOf(55));
        when(deliveryComputationProperties.getConditionReject())
                .thenReturn(REJECT_WEIGHT_CONDITION_IN_KG);

        // when
        final DeliveryItemException exception = assertThrows(DeliveryItemException.class,
                ()-> cut.computeDeliveryCost(deliveryDetails, VoucherCodeDTO.MYNT));

        // then
        assertEquals("Item weight is exceeding allowed weight", exception.getMessage());
    }

    @Test
    void computeDeliveryCost_validDeliveryDetails_triggerHeavyParcelRule() {
        // given
        var deliveryDetails = createDeliveryDetailsDTO(Double.valueOf(11));
        when(deliveryComputationProperties.getConditionReject())
                .thenReturn(REJECT_WEIGHT_CONDITION_IN_KG);
        when(deliveryComputationProperties.getConditionHeavyParcel())
                .thenReturn(LARGE_WEIGHT_CONDITION_IN_KG);

        when(deliveryComputationProperties.getCostHeavyParcel())
                .thenReturn(COST_MULTIPLIER);

        // when
        var result = cut.computeDeliveryCost(deliveryDetails, VoucherCodeDTO.MYNT);

        // then
        assertNotNull(result);
        assertEquals(new BigDecimal("11.00"), result.getCost()); // 11(WEIGHT) * 1
    }

    @Test
    void computeDeliveryCost_validDeliveryDetails_triggerSmallParcelRule() {
        // given
        var deliveryDetails = createDeliveryDetailsDTO(Double.valueOf(8));
        when(deliveryComputationProperties.getConditionReject())
                .thenReturn(REJECT_WEIGHT_CONDITION_IN_KG);
        when(deliveryComputationProperties.getConditionHeavyParcel())
                .thenReturn(LARGE_WEIGHT_CONDITION_IN_KG);
        when(deliveryComputationProperties.getConditionSmallParcel())
                .thenReturn(SMALL_VOLUME_CONDITION_IN_CM3);

        when(deliveryComputationProperties.getCostSmallParcel())
                .thenReturn(COST_MULTIPLIER);

        // when
        var result = cut.computeDeliveryCost(deliveryDetails, VoucherCodeDTO.MYNT);

        // then
        assertNotNull(result);
        assertEquals(new BigDecimal("20.00"), result.getCost()); // (10*2*1)volume * 1
    }

    @Test
    void computeDeliveryCost_validDeliveryDetails_triggerMediumParcelRule() {
        // given
        var deliveryDetails = createDeliveryDetailsDTO(Double.valueOf(8), Double.valueOf(110));
        when(deliveryComputationProperties.getConditionReject())
                .thenReturn(REJECT_WEIGHT_CONDITION_IN_KG);
        when(deliveryComputationProperties.getConditionHeavyParcel())
                .thenReturn(LARGE_WEIGHT_CONDITION_IN_KG);
        when(deliveryComputationProperties.getConditionSmallParcel())
                .thenReturn(SMALL_VOLUME_CONDITION_IN_CM3);
        when(deliveryComputationProperties.getConditionMediumParcel())
                .thenReturn(MEDIUM_VOLUME_CONDITION_IN_CM3);

        when(deliveryComputationProperties.getCostMediumParcel())
                .thenReturn(COST_MULTIPLIER);

        // when
        var result = cut.computeDeliveryCost(deliveryDetails, VoucherCodeDTO.MYNT);

        // then
        assertNotNull(result);
        assertEquals(new BigDecimal("2200.00"), result.getCost()); // (10*2*110)volume * 1
    }

    @Test
    void computeDeliveryCost_validDeliveryDetails_triggerLargeParcelRule() {
        // given
        var deliveryDetails = createDeliveryDetailsDTO(Double.valueOf(8), Double.valueOf(200));
        when(deliveryComputationProperties.getConditionReject())
                .thenReturn(REJECT_WEIGHT_CONDITION_IN_KG);
        when(deliveryComputationProperties.getConditionHeavyParcel())
                .thenReturn(LARGE_WEIGHT_CONDITION_IN_KG);
        when(deliveryComputationProperties.getConditionSmallParcel())
                .thenReturn(SMALL_VOLUME_CONDITION_IN_CM3);
        when(deliveryComputationProperties.getConditionMediumParcel())
                .thenReturn(MEDIUM_VOLUME_CONDITION_IN_CM3);

        when(deliveryComputationProperties.getCostLargeParcel())
                .thenReturn(COST_MULTIPLIER);

        // when
        var result = cut.computeDeliveryCost(deliveryDetails, VoucherCodeDTO.MYNT);

        // then
        assertNotNull(result);
        assertEquals(new BigDecimal("4000.00"), result.getCost()); // (10*2*200)volume * 1
    }

    private DeliveryDetailsDTO createDeliveryDetailsDTO(Double weight) {
        return createDeliveryDetailsDTO(weight, Double.valueOf(1));
    }

    private DeliveryDetailsDTO createDeliveryDetailsDTO(Double weight, Double height) {
        return DeliveryDetailsDTO.builder()
                .weight(weight)
                .length(Double.valueOf(10))
                .width(Double.valueOf(2))
                .height(height)
                .build();
    }


}
