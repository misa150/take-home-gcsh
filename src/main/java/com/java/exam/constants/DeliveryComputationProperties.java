package com.java.exam.constants;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "delivery.computation")
@Getter
@Setter
public class DeliveryComputationProperties {

    private Integer conditionReject;
    private Integer conditionHeavyParcel;
    private Integer conditionSmallParcel;
    private Integer conditionMediumParcel;

    private Double costHeavyParcel;
    private Double costSmallParcel;
    private Double costMediumParcel;
    private Double costLargeParcel;

}
