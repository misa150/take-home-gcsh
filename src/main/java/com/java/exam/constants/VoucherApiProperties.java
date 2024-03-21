package com.java.exam.constants;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "voucher.api")
@Getter
@Setter
public class VoucherApiProperties {

    private String url;
    private String apikey;
    private Boolean useMock;

}
