package com.indi.eventapi.models;

public enum Company {

    PEP("Pepsi Co."),
    KO("Coca Cola"),
    APPL("Apple Inc."),
    MSFT("Microsoft Corporation"),
    GOOGL("Alphabet Inc."),
    TSLA("TESLA Inc.");

    private Company(String name) {
        this.name = name;
    }

    public final String name;
}
