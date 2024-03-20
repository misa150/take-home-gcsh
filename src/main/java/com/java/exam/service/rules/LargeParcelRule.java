package com.java.exam.service.rules;


import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LargeParcelRule implements DeliveryRule {

    @Override
    public BigDecimal apply() {
        return null;
    }
}
