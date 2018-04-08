package us.hgmtrebing.calculator;

import lombok.Getter;

import java.math.BigDecimal;

public class Calculator {

    @Getter private final CalculatorHistory history = new CalculatorHistory();
    @Getter private StringBuilder expression = new StringBuilder();

    public BigDecimal evaluate (String expression) {
        BigDecimal result = Tokenizer.evaluate(expression);
        history.addHistoryItem(expression, result);
        return result;
    }

    public BigDecimal evaluate () {
        BigDecimal result = this.evaluate( this.expression.toString() );
        this.expression.delete (0, this.expression.length());
        return result;
    }

}
