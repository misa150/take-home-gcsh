package com.java.exam.controller;

import com.java.exam.dto.DeliveryCostDTO;
import com.java.exam.dto.DeliveryDetailsDTO;
import com.java.exam.dto.VoucherCodeDTO;
import com.java.exam.service.DeliveryCostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("delivery-cost")
@RequiredArgsConstructor
public class DeliveryCostController {

    private final DeliveryCostService deliveryCostService;

    @GetMapping(value = {"/compute/{voucherCode}"})
    public DeliveryCostDTO computeDeliveryCost(@RequestBody DeliveryDetailsDTO deliveryDetails,
                                               @PathVariable("voucherCode") VoucherCodeDTO voucherCode) {
        return deliveryCostService.computeDeliveryCost(deliveryDetails, voucherCode);
    }
}
