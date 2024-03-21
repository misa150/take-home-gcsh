package com.java.exam.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VoucherItem {

    private String code;
    private Integer discount;
    private LocalDate expiry;

}
