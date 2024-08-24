package com.indi.eventapi.models;

import com.indi.eventapi.dto.StockDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Stock {

    private Company company;

    private String code;

    private Float adjClose;

    private Float open;

    private Float close;

    private Float high;

    private Float low;

    private Integer volume;

    public static Stock toStock(StockDto stockDto) {
        return Stock.builder()
                .company(Company.valueOf(stockDto.getCode()))
                .code(stockDto.getCode())
                .adjClose(stockDto.getAdjClose())
                .open(stockDto.getOpen())
                .close(stockDto.getClose())
                .high(stockDto.getHigh())
                .low(stockDto.getLow())
                .volume(stockDto.getVolume())
                .build();
    }
}
