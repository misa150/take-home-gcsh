package com.java.exam.service.voucher;

import com.java.exam.constants.VoucherApiProperties;
import com.java.exam.dto.VoucherCodeDTO;
import com.java.exam.exception.voucher.VoucherApiException;
import com.java.exam.models.VoucherItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherApiService {

    private final RestTemplate restTemplate;
    private final VoucherApiProperties voucherApiProperties;

    public VoucherItem callVoucherApi(VoucherCodeDTO voucherCode) {
        try {
            return restTemplate.getForObject(buildUrL(voucherCode.toString()), VoucherItem.class);
        } catch (RestClientException ex) {
            log.warn("Error calling Voucher API {}", ex.getMessage());
            throw new VoucherApiException(ex.getMessage());
        }
    }

    private String buildUrL(String voucherCode) {
        return UriComponentsBuilder.fromUriString(voucherApiProperties.getUrl())
                .queryParam("key", voucherApiProperties.getApikey())
                .buildAndExpand(voucherCode)
                .toUriString();
    }
}
