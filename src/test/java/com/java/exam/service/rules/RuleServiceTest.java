package com.java.exam.service.rules;

import com.java.exam.constants.DeliveryComputationProperties;
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
class RuleServiceTest {
    private static final Double COST_MULTIPLIER = Double.valueOf(1);

    private static final Double REJECT_WEIGHT_CONDITION_IN_KG = Double.valueOf(50);
    private static final Double LARGE_WEIGHT_CONDITION_IN_KG = Double.valueOf(10);
    private static final Double SMALL_VOLUME_CONDITION_IN_CM3 = Double.valueOf(1500);
    private static final Double MEDIUM_VOLUME_CONDITION_IN_CM3 = Double.valueOf(2500);

    private RuleService cut;

    @Mock
    private DeliveryComputationProperties deliveryComputationProperties;

    @BeforeEach
    private void setup() {
        cut = new RuleService(deliveryComputationProperties);
    }

    @Test
    void computeDeliveryCost_validDeliveryDetails_triggerRejectRule() {
        // given
        var weight = Double.valueOf(55);
        var volume = Double.valueOf(100);
        when(deliveryComputationProperties.getConditionReject())
                .thenReturn(REJECT_WEIGHT_CONDITION_IN_KG);

        // when
        final DeliveryItemException exception = assertThrows(DeliveryItemException.class,
                ()-> cut.determineRuleAndGetCost(weight, volume));

        // then
        assertEquals("Item weight is exceeding allowed weight", exception.getMessage());
    }

    @Test
    void computeDeliveryCost_validDeliveryDetails_triggerHeavyParcelRule() {
        // given
        var weight = Double.valueOf(11);
        var volume = Double.valueOf(100);
        when(deliveryComputationProperties.getConditionReject())
                .thenReturn(REJECT_WEIGHT_CONDITION_IN_KG);
        when(deliveryComputationProperties.getConditionHeavyParcel())
                .thenReturn(LARGE_WEIGHT_CONDITION_IN_KG);

        when(deliveryComputationProperties.getCostHeavyParcel())
                .thenReturn(COST_MULTIPLIER);

        // when
        var result = cut.determineRuleAndGetCost(weight, volume);

        // then
        assertNotNull(result);
        assertEquals(new BigDecimal("11.00"), result);
    }

    @Test
    void computeDeliveryCost_validDeliveryDetails_triggerSmallParcelRule() {
        // given
        var weight = Double.valueOf(8);
        var volume = Double.valueOf(100);
        when(deliveryComputationProperties.getConditionReject())
                .thenReturn(REJECT_WEIGHT_CONDITION_IN_KG);
        when(deliveryComputationProperties.getConditionHeavyParcel())
                .thenReturn(LARGE_WEIGHT_CONDITION_IN_KG);
        when(deliveryComputationProperties.getConditionSmallParcel())
                .thenReturn(SMALL_VOLUME_CONDITION_IN_CM3);

        when(deliveryComputationProperties.getCostSmallParcel())
                .thenReturn(COST_MULTIPLIER);

        // when
        var result = cut.determineRuleAndGetCost(weight, volume);

        // then
        assertNotNull(result);
        assertEquals(new BigDecimal("100.00"), result);
    }

    @Test
    void computeDeliveryCost_validDeliveryDetails_triggerMediumParcelRule() {
        // given
        var weight = Double.valueOf(8);
        var volume = Double.valueOf(2200);
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
        var result = cut.determineRuleAndGetCost(weight, volume);

        // then
        assertNotNull(result);
        assertEquals(new BigDecimal("2200.00"), result);
    }

    @Test
    void computeDeliveryCost_validDeliveryDetails_triggerLargeParcelRule() {
        // given
        var weight = Double.valueOf(8);
        var volume = Double.valueOf(4000);
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
        var result = cut.determineRuleAndGetCost(weight, volume);

        // then
        assertNotNull(result);
        assertEquals(new BigDecimal("4000.00"), result);
    }

}
