package com.quickresto.spreadsheet.controller;

import com.quickresto.spreadsheet.service.SpreadsheetService;
import com.quickresto.spreadsheet.util.ExpressionEvaluator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class SpreadsheetController {

    private final SpreadsheetService spreadsheetService;
    private final ExpressionEvaluator expressionEvaluator;

    public SpreadsheetController(SpreadsheetService spreadsheetService, ExpressionEvaluator expressionEvaluator) {
        this.spreadsheetService = spreadsheetService;
        this.expressionEvaluator = expressionEvaluator;
    }

    @ModelAttribute("cells")
    public Map<String, Double> getCells() {
        return spreadsheetService.getCells();
    }

    @GetMapping("/")
    public String showSpreadsheet(Model model) {
        return "index";
    }

    @PostMapping("/calculate")
    public String calculateCells(@RequestParam Map<String, String> cellValues) {
        Map<String, Double> cells = spreadsheetService.getCells();

        for (String cellAddress : cellValues.keySet()) {
            String cellValue = cellValues.get(cellAddress);
            if (cellValue.startsWith("=")) {
                String expression = cellValue.substring(1);
                double result = expressionEvaluator.evaluateExpression(expression);
                cells.put(cellAddress, result);
            } else {
                double numericValue = Double.parseDouble(cellValue);
                cells.put(cellAddress, numericValue);
            }
        }

        return "redirect:/";
    }
}
