package com.java.exam.service.voucher;

import com.java.exam.constants.VoucherApiProperties;
import com.java.exam.dto.VoucherCodeDTO;
import com.java.exam.exception.voucher.VoucherApiException;
import com.java.exam.models.VoucherItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoucherApiServiceTest {

    private static final String URL = "url/{voucherCode}";
    private static final String APIKEY = "api";
    private static final String FINAL_URL = "url/MYNT?key=api";
    private static final VoucherCodeDTO voucherCode= VoucherCodeDTO.MYNT;

    private VoucherApiService cut;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private VoucherApiProperties voucherApiProperties;

    @BeforeEach
    private void setup() {
        cut = new VoucherApiService(restTemplate, voucherApiProperties);

        when(voucherApiProperties.getUrl()).thenReturn(URL);
        when(voucherApiProperties.getApikey()).thenReturn(APIKEY);
    }

    @Test
    void callVoucherApi_shouldBeOk() {
        // given
        when(restTemplate.getForObject(FINAL_URL, VoucherItem.class)).thenReturn(new VoucherItem());

        // when
        var result = cut.callVoucherApi(voucherCode);

        // then
        assertNotNull(result);
    }

    @Test
    void callVoucherApi_exceptionEncountered_shouldThrowError() {
        // given
        var errorMessage = "rest api error";
        when(restTemplate.getForObject(FINAL_URL, VoucherItem.class))
                .thenThrow(new RestClientException(errorMessage));

        // when
        VoucherApiException exception = assertThrows(VoucherApiException.class,
                ()-> cut.callVoucherApi(voucherCode));

        // then
        assertEquals(errorMessage, exception.getMessage());
    }

}