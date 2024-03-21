package com.java.exam.service;

import com.java.exam.dto.DeliveryDetailsDTO;
import com.java.exam.dto.VoucherCodeDTO;
import com.java.exam.exception.delivery.DeliveryItemException;
import com.java.exam.service.rules.RuleService;
import com.java.exam.service.voucher.VoucherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryCostServiceTest {

    private DeliveryCostService cut;

    @Mock
    private RuleService ruleService;

    @Mock
    private VoucherService voucherService;

    @BeforeEach
    private void setup() {
        cut = new DeliveryCostService(ruleService, voucherService);
    }

    @Test
    void computeDeliveryCost_inValidDeliveryDetails_throwValidationError() {
        // when
        DeliveryItemException exception = assertThrows(DeliveryItemException.class,
                ()-> cut.computeDeliveryCost(null, VoucherCodeDTO.MYNT));

        // then
        assertEquals("Delivery Item information has null values", exception.getMessage());

        verifyNoInteractions(ruleService);
        verifyNoInteractions(voucherService);
    }

    @Test
    void computeDeliveryCost_validDeliveryDetailsNotUsingVoucher_shouldReturnOk() {
        // given
        var deliveryDetails = createDeliveryDetailsDTO();
        var cost = BigDecimal.TEN;
        when(ruleService.determineRuleAndGetCost(Double.valueOf(10), Double.valueOf(20)))
                .thenReturn(cost);

        // when
        var result = cut.computeDeliveryCost(deliveryDetails, VoucherCodeDTO.NONE);

        // then
        assertNotNull(result);
        assertEquals(cost, result.getCost());

        verifyNoInteractions(voucherService);
    }

    @Test
    void computeDeliveryCost_validDeliveryDetailsUsingVoucher_shouldReturnOk() {
        // given
        var deliveryDetails = createDeliveryDetailsDTO();
        var cost = BigDecimal.TEN;
        var voucher = VoucherCodeDTO.MYNT;
        when(ruleService.determineRuleAndGetCost(Double.valueOf(10), Double.valueOf(20)))
                .thenReturn(cost);
        when(voucherService.useVoucher(cost, voucher)).thenReturn(cost);

        // when
        var result = cut.computeDeliveryCost(deliveryDetails, voucher);

        // then
        assertNotNull(result);
        assertEquals(cost, result.getCost());

        verify(voucherService).useVoucher(cost, voucher);
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
