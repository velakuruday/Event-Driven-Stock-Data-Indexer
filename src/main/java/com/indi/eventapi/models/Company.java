package com.indi.eventapi.models;

import java.util.HashMap;

public class Company {
    public static HashMap<String, String> codeMap = new HashMap<String, String>() {{
        put("PEP", "Pepsi Co.");
        put("KO", "Coca Cola");
        put("AAPL", "Apple Inc.");
        put("MSFT", "Microsoft Corporation");
        put("GOOGL", "Alphabet Inc.");
        put("TSLA", "TESLA Inc.");
    }};
}
