package us.hgmtrebing.calculator;

import lombok.Getter;
import java.math.BigDecimal;

public enum TokenType {

    VALUE ("(((?<=\\D)|^)-)?\\d+(\\.\\d+)?", 0, 0, true, false, false) {
        @Override
        public BigDecimal evaluate (BigDecimal ... operands) {
            throw new UnsupportedOperationException("Value cannot be evaluated");
        }
    },

    ADD ("\\+", 1, 2, false, true, true) {
        @Override
        public BigDecimal evaluate (BigDecimal... operands) {
            BigDecimal result = new BigDecimal(0);
            for (BigDecimal o : operands) {
                result = result.add(o);
            }

            return result;
        }
    },

    SUBTRACT ("(?<=\\d)?\\-", 1, 2, false, true, true) {
        @Override
        public BigDecimal evaluate (BigDecimal ... operands) {
            BigDecimal result = new BigDecimal(0);
            for (BigDecimal o : operands) {
                result = result.subtract(o);
            }
            return result;
        }
    },

    MULTIPLY ("[\\*×⋅]", 2, 2, false, true, true) {
        @Override
        public  BigDecimal evaluate (BigDecimal ... operands) {
            BigDecimal result = new BigDecimal(1);
            for (BigDecimal o: operands) {
                result = result.multiply(o);
            }
            return result;
        }
    },

    DIVIDE ("[/÷]", 2, 2, false, true, true) {
        @Override
        public BigDecimal evaluate (BigDecimal ... operands) {
            BigDecimal result = operands[0];
            for (int i = 1; i < operands.length; i++) {
                result = result.divide(operands[i]);
            }
            return result;
        }
    },

    UNKNOWN("", 100_000, 0, false, false, false) {
        @Override
        public BigDecimal evaluate (BigDecimal ... operands) {
            throw new UnsupportedOperationException("Invalid Token!");
        }
    }

    ;

    @Getter private final String symbols;
    @Getter private final int precedence;
    @Getter private final int operandNum;
    @Getter private final boolean containsValue;
    @Getter private final boolean evaluatable;
    @Getter private final boolean leftAssociative;

    public abstract BigDecimal evaluate (BigDecimal ... operands);

    TokenType (String symbols, int precedence, int operandNum, boolean containsValue, boolean evaluatable, boolean
                      leftAssociative) {

        this.symbols = symbols;
        this.precedence = precedence;
        this.operandNum = operandNum;
        this.containsValue = containsValue;
        this.evaluatable = evaluatable;
        this.leftAssociative = leftAssociative;
    }
}
