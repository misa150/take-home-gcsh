package com.java.exam.controller;

import com.java.exam.dto.DeliveryCostDTO;
import com.java.exam.dto.DeliveryDetailsDTO;
import com.java.exam.dto.VoucherCodeDTO;
import com.java.exam.service.DeliveryCostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DeliveryCostControllerTest {

    private DeliveryCostController cut;

    @Mock
    private DeliveryCostService deliveryCostService;

    @BeforeEach
    private void setup() {
        cut = new DeliveryCostController(deliveryCostService);
    }

    @Test
    void computeDeliveryCost_ShouldReturnOk() {
        // given
        var deliveryDetails = createDeliveryDetailsDTO();
        when(deliveryCostService.computeDeliveryCost(deliveryDetails, VoucherCodeDTO.MYNT))
                .thenReturn(createDeliveryCostDTO());

        // when
        var result = cut.computeDeliveryCost(deliveryDetails, VoucherCodeDTO.MYNT);

        // then
        assertNotNull(result);
    }

    private DeliveryCostDTO createDeliveryCostDTO() {
        return DeliveryCostDTO.builder()
                .cost(BigDecimal.ONE)
                .build();
    }

    private DeliveryDetailsDTO createDeliveryDetailsDTO() {
        return DeliveryDetailsDTO.builder()
                .weight(Double.valueOf(50))
                .length(Double.valueOf(10))
                .width(Double.valueOf(5))
                .height(Double.valueOf(4))
                .build();
    }


}
