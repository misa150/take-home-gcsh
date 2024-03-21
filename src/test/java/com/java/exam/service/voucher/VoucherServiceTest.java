package com.java.exam.service.voucher;

import com.java.exam.constants.VoucherApiProperties;
import com.java.exam.dto.VoucherCodeDTO;
import com.java.exam.exception.voucher.VoucherExpiredException;
import com.java.exam.models.VoucherItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoucherServiceTest {

    private static final BigDecimal INITIAL_COST = BigDecimal.valueOf(500);

    private VoucherService cut;

    @Mock
    private VoucherApiService voucherApiService;

    @Mock
    private  VoucherApiProperties voucherApiProperties;

    @BeforeEach
    private void setup() {
        cut = new VoucherService(voucherApiService, voucherApiProperties);
    }

    @Test
    void callVoucherApi_usingMockServiceMYNTVoucherCode_shouldBeOk() {
        // given
        when(voucherApiProperties.getUseMock()).thenReturn(true);

        // when
        var result = cut.useVoucher(INITIAL_COST, VoucherCodeDTO.MYNT);

        // then
        assertEquals(BigDecimal.valueOf(490), result);

        verifyNoInteractions(voucherApiService);
    }

    @Test
    void callVoucherApi_usingMockServiceGFIVoucherCode_shouldBeOk() {
        // given
        when(voucherApiProperties.getUseMock()).thenReturn(true);

        // when
        var result = cut.useVoucher(INITIAL_COST, VoucherCodeDTO.GFI);

        // then
        assertEquals(BigDecimal.valueOf(480), result);

        verifyNoInteractions(voucherApiService);
    }

    @Test
    void callVoucherApi_usingMockServiceskdlksVoucherCode_shouldThrowException() {
        // given
        when(voucherApiProperties.getUseMock()).thenReturn(true);

        // when
        VoucherExpiredException exception = assertThrows(VoucherExpiredException.class,
                ()-> cut.useVoucher(INITIAL_COST, VoucherCodeDTO.skdlks));

        // then
        assertEquals("Voucher has expired", exception.getMessage());

        verifyNoInteractions(voucherApiService);
    }

    @Test
    void callVoucherApi_useVoucherApi_shouldBeOk() {
        // given
        var voucherCode = VoucherCodeDTO.GFI;
        VoucherItem voucherItem = getVoucherItem(voucherCode, 30, LocalDate.now().plusWeeks(2));

        when(voucherApiProperties.getUseMock()).thenReturn(false);
        when(voucherApiService.callVoucherApi(voucherCode)).thenReturn(voucherItem);

        // when
        var result = cut.useVoucher(INITIAL_COST, voucherCode);

        // then
        assertEquals(BigDecimal.valueOf(470), result);

        verify(voucherApiService).callVoucherApi(voucherCode);
    }

    @Test
    void callVoucherApi_useVoucherApiReturnIsExpiredVoucher_shouldThrowException() {
        // given
        var voucherCode = VoucherCodeDTO.skdlks;
        VoucherItem voucherItem = getVoucherItem(voucherCode, 50, LocalDate.now().minusWeeks(2));

        when(voucherApiProperties.getUseMock()).thenReturn(false);
        when(voucherApiService.callVoucherApi(voucherCode)).thenReturn(voucherItem);

        // when
        VoucherExpiredException exception = assertThrows(VoucherExpiredException.class,
                ()-> cut.useVoucher(INITIAL_COST, voucherCode));

        // then
        assertEquals("Voucher has expired", exception.getMessage());

        verify(voucherApiService).callVoucherApi(voucherCode);
    }

    private VoucherItem getVoucherItem(VoucherCodeDTO voucherCode, Integer discount, LocalDate expiry) {
        VoucherItem voucherItem = new VoucherItem();
        voucherItem.setCode(voucherCode.toString());
        voucherItem.setDiscount(discount);
        voucherItem.setExpiry(expiry);
        return voucherItem;
    }
}
