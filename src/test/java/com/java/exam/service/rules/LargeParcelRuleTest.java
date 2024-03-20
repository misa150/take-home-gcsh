package com.java.exam.service.rules;

import com.java.exam.constants.DeliveryComputationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LargeParcelRuleTest {

    private LargeParcelRule cut;

    @Mock
    private DeliveryComputationProperties deliveryComputationProperties;

    @BeforeEach
    private void setup() {
        cut = new LargeParcelRule(deliveryComputationProperties);
    }

    @Test
    void computeCost_ShouldReturnCost() {
        // given
        var computationBasis = Double.valueOf(1);
        when(deliveryComputationProperties.getCostLargeParcel())
                .thenReturn(Double.valueOf(4));

        // when
        var result = cut.computeCost(computationBasis);

        // then
        assertEquals(new BigDecimal("4.00"), result);
    }

}
