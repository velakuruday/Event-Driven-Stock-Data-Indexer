package com.indi.eventapi.models;

import com.indi.eventapi.dto.StockUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdate {

    private String timestamp;

    private List<Stock> stocks;

    public static StockUpdate toStockUpdate(StockUpdateDto stockUpdateDto) {
        return StockUpdate.builder()
                .timestamp(stockUpdateDto.getTimestamp())
                .stocks(stockUpdateDto.getStockList().stream().map(Stock::toStock).collect(Collectors.toList()))
                .build();
    }
}
