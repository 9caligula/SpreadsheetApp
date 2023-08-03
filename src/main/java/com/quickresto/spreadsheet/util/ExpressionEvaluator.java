package com.quickresto.spreadsheet.util;

import com.quickresto.spreadsheet.service.SpreadsheetService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Stack;

@Component
public class ExpressionEvaluator {

    private final SpreadsheetService service;

    public ExpressionEvaluator(SpreadsheetService service) {
        this.service = service;
    }

    private int precedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> -1;
        };
    }

    public double evaluateExpression(String expression) {
        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        Map<String, Double> cells = service.getCells();
        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch)) {
                StringBuilder numBuilder = new StringBuilder();
                numBuilder.append(ch);
                while (i + 1 < expression.length() && (Character.isDigit(expression.charAt(i + 1)) ||
                        expression.charAt(i + 1) == '.')) {

                    numBuilder.append(expression.charAt(i + 1));
                    i++;
                }
                values.push(Double.parseDouble(numBuilder.toString()));
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop();
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(ch)) {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(ch);
            } else {
                String key = String.valueOf(ch) + expression.charAt(i + 1);
                if (cells.containsKey(key.toUpperCase())) {
                    values.push(cells.get(key));
                    i++;
                }
            }
        }

        while (!operators.isEmpty()) {
            values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private double applyOperator(char operator, double b, double a) {
        switch (operator) {
            case '+' -> {
                return a + b;
            }
            case '-' -> {
                return a - b;
            }
            case '*' -> {
                return a * b;
            }
            case '/' -> {
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
            }
        }
        return 0;
    }
}
