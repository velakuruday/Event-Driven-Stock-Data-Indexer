package com.indi.eventapi.models;

import com.indi.eventapi.dto.StockUpdateDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class StockUpdate {

    private String timestamp;

    private List<Stock> stocks;

    public static StockUpdate toStockUpdate(StockUpdateDto stockUpdateDto) {
        return StockUpdate.builder()
                .timestamp(stockUpdateDto.getTimestamp())
                .stocks(stockUpdateDto.getStocks().stream().map(Stock::toStock).collect(Collectors.toList()))
                .build();
    }
}
