package com.java.exam.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeliveryDetailsDTO {

    private Double weight;
    private Double height;
    private Double width;
    private Double length;

}
