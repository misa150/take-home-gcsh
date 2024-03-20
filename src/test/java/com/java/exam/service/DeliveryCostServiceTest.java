package com.java.exam.service;

import com.java.exam.constants.DeliveryComputationProperties;
import com.java.exam.dto.DeliveryDetailsDTO;
import com.java.exam.dto.VoucherCodeDTO;
import com.java.exam.exception.DeliveryItemException;
import com.java.exam.service.rules.RuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryCostServiceTest {

    private DeliveryCostService cut;

    @Mock
    private RuleService ruleService;

    @BeforeEach
    private void setup() {
        cut = new DeliveryCostService(ruleService);
    }

    @Test
    void computeDeliveryCost_inValidDeliveryDetails_throwValidationError() {
        // when
        final DeliveryItemException exception = assertThrows(DeliveryItemException.class,
                ()-> cut.computeDeliveryCost(null, VoucherCodeDTO.MYNT));

        // then
        assertEquals("Delivery Item information has null values", exception.getMessage());

        verifyNoInteractions(ruleService);
    }

    @Test
    void computeDeliveryCost_validDeliveryDetails_shouldReturnOk() {
        // given
        var deliveryDetails = createDeliveryDetailsDTO();
        var cost = BigDecimal.TEN;
        when(ruleService.determineRuleAndGetCost(Double.valueOf(10), Double.valueOf(20)))
                .thenReturn(cost);

        // when
        var result = cut.computeDeliveryCost(deliveryDetails, VoucherCodeDTO.MYNT);

        // then
        assertNotNull(result);
        assertEquals(cost, result.getCost());
    }

    private DeliveryDetailsDTO createDeliveryDetailsDTO() {
        return DeliveryDetailsDTO.builder()
                .weight(Double.valueOf(10))
                .length(Double.valueOf(1))
                .width(Double.valueOf(2))
                .height(Double.valueOf(10))
                .build();
    }


}
