package com.quickresto.spreadsheet.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SpreadsheetService {

    private final Map<String, Double> cells;

    public SpreadsheetService() {
        cells = new HashMap<>();
        initializeCells();
    }

    public Map<String, Double> getCells() {
        return cells;
    }

    private void initializeCells() {
        for (int row = 1; row <= 4; row++) {
            for (char col = 'A'; col <= 'D'; col++) {
                String cellAddress = col + String.valueOf(row);
                cells.put(cellAddress, 0.0);
            }
        }
    }
}

