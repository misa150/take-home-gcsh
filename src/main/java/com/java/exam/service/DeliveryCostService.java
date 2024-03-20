package com.java.exam.service;


import com.java.exam.constants.DeliveryComputationProperties;
import com.java.exam.dto.DeliveryCostDTO;
import com.java.exam.dto.DeliveryDetailsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryCostService {

    private final DeliveryComputationProperties deliveryComputationProperties;


    public DeliveryCostDTO computeDeliveryCost(DeliveryDetailsDTO deliveryDetails) {
        return null;
    }
}
