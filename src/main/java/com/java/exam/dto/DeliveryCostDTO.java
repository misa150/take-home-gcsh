package com.java.exam.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
public class DeliveryCostDTO {

    private BigDecimal cost;

}
