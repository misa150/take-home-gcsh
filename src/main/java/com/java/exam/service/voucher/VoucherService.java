package com.java.exam.service.voucher;


import com.java.exam.constants.VoucherApiProperties;
import com.java.exam.dto.VoucherCodeDTO;
import com.java.exam.exception.voucher.VoucherExpiredException;
import com.java.exam.models.VoucherItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherApiService voucherApiService;
    private final VoucherApiProperties voucherApiProperties;

    public BigDecimal useVoucher(BigDecimal cost, VoucherCodeDTO voucherCode) {
        VoucherItem voucherItem = null;

        if(voucherApiProperties.getUseMock()) {
            log.info("Using Voucher mock service");
            voucherItem = useMockVoucher(voucherCode);
        } else {
            log.info("Calling Voucher API");
            voucherItem = voucherApiService.callVoucherApi(voucherCode);
        }

        validateIfVoucherIsValid(voucherItem);

        return applyVoucher(cost, voucherItem.getDiscount());
    }

    private BigDecimal applyVoucher(BigDecimal cost, Integer discount) {
        var newCost = cost.subtract(BigDecimal.valueOf(discount));
        return newCost.compareTo(BigDecimal.ZERO) > 0 ?
                newCost :
                BigDecimal.ZERO;
    }

    private void validateIfVoucherIsValid(VoucherItem voucherItem) {
        if(voucherItem.getExpiry().isBefore(LocalDate.now())) {
            String errorMessage = "Voucher has expired";
            log.warn(errorMessage);
            throw new VoucherExpiredException(errorMessage);
        }
    }

    //MOCK IMPLEMENTATIONS
    private VoucherItem useMockVoucher(VoucherCodeDTO voucherCode) {
        VoucherItem voucherItem = new VoucherItem();

        if(voucherCode.equals(VoucherCodeDTO.MYNT)) {
            populateVoucherItem(voucherItem, VoucherCodeDTO.MYNT, 10, LocalDate.now().plusWeeks(1));
        } else if(voucherCode.equals(VoucherCodeDTO.GFI)) {
            populateVoucherItem(voucherItem, VoucherCodeDTO.GFI, 20, LocalDate.now().plusWeeks(2));
        } else if(voucherCode.equals(VoucherCodeDTO.skdlks)) {
            populateVoucherItem(voucherItem, VoucherCodeDTO.skdlks, 30, LocalDate.now().minusWeeks(2));
        }
        return voucherItem;
    }

    private void populateVoucherItem(VoucherItem voucherItem, VoucherCodeDTO voucher, Integer discount, LocalDate expiry) {
        voucherItem.setCode(voucher.toString());
        voucherItem.setDiscount(discount);
        voucherItem.setExpiry(expiry);
    }

}
