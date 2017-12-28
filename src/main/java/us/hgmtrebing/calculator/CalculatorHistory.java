package us.hgmtrebing.calculator;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class CalculatorHistory {

    private final List<HistoryItem> historyItems = new ArrayList<>();

    public BigDecimal getResult (int index) {
        return historyItems.get(index).getResult();
    }

    public String getExpression (int index) {
        return historyItems.get(index).getExpression();
    }

    public int length () {
        return this.historyItems.size();
    }

    public void addHistoryItem (HistoryItem h) {
        historyItems.add(h);
    }

    public void addHistoryItem (String expression, BigDecimal result) {
        addHistoryItem(new HistoryItem(expression, result));
    }

    private class HistoryItem {
        @Getter
        private final String expression;
        @Getter private final BigDecimal result;

        HistoryItem (String expression, BigDecimal result) {
            this.expression = expression;
            this.result = result;
        }
    }
}
