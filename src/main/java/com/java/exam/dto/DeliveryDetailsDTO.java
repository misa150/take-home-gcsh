package com.java.exam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryDetailsDTO {

    private DeliveryItemInformationDTO deliveryItemsInfo;
    private String voucherCode;

}
