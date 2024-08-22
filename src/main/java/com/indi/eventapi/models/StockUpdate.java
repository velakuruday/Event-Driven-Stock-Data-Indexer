package com.indi.eventapi.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StockUpdate {

    private String timestamp;

    private List<Stock> stocks;
}
