package com.quickresto.spreadsheet.util;

import com.quickresto.spreadsheet.service.SpreadsheetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;


class ExpressionEvaluatorTest {

    @Mock
    private SpreadsheetService service;

    private ExpressionEvaluator underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new ExpressionEvaluator(service);
    }

    @Test
    void canSimpleEvaluateExpression() {
        double actual = underTest.evaluateExpression("5 + 5");

        var expected = 10;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void canSimpleEvaluateExpressionWithMoreOperators() {
        double actual = underTest.evaluateExpression("23 + 8 - 19 / 2 - 110");

        var expected = -88.5;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void canSimpleEvaluateExpressionWithMoreOperators2() {
        double actual = underTest.evaluateExpression("(8 + 89) * 5 - 17 / 2");

        var expected = 476.5;
        assertThat(actual).isEqualTo(expected);
    }
}