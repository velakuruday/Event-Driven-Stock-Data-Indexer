package com.indi.eventapi.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Stock {

    private String company;

    private String code;

    private Float adjClose;

    private Float open;

    private Float close;

    private Float high;

    private Float low;

    private Integer volume;

}
