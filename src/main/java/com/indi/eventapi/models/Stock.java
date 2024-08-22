package com.indi.eventapi.models;

import com.indi.eventapi.dto.StockDto;
import lombok.Builder;
import lombok.Data;
import org.rocksdb.Status;

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

    public static Stock toStock(StockDto stockDto) {
        return Stock.builder()
                .company(CodeMap.codeMap.get(stockDto.getCode()))
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
