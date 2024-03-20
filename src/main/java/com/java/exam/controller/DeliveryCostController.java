package com.java.exam.controller;

import com.java.exam.dto.DeliveryCostDTO;
import com.java.exam.dto.DeliveryDetailsDTO;
import com.java.exam.service.DeliveryCostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("delivery-cost")
@RequiredArgsConstructor
public class DeliveryCostController {

    private final DeliveryCostService deliveryCostService;


    @PostMapping(value = {"/compute"})
    public DeliveryCostDTO computeDeliveryCost(@RequestBody DeliveryDetailsDTO deliveryDetails) {
        return deliveryCostService.computeDeliveryCost(deliveryDetails);
    }
}
