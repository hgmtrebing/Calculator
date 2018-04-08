package us.hgmtrebing.calculator;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * The enum class TokenType represents the various types of Tokens that can be encountered when parsing
 * a mathematical expression.
 * With the exception of the special TokenType.VALUE (which represents numeric values) and TokenType.UNKNOWN,
 * TokenTypes represent mathematical operators.
 */
public enum TokenType {

    VALUE ("(((?<=[\\D&&[^\\(\\)]])|^)-)?((\\d+(\\.\\d+)?)|(\\.\\d+){1})", null, 0, 0, true, false, false) {
        @Override
        public BigDecimal evaluate (BigDecimal ... operands) {
            throw new UnsupportedOperationException("Value cannot be evaluated");
        }
    },

    ADD ("\\+", "+", 1, 2, false, true, true) {
        @Override
        public BigDecimal evaluate (BigDecimal... operands) {
            BigDecimal result = new BigDecimal(0);
            for (BigDecimal o : operands) {
                result = result.add(o);
            }

            return result;
        }
    },

    SUBTRACT ("(?<=\\d|\\))?\\-", "-", 1, 2, false, true, true) {
        @Override
        public BigDecimal evaluate (BigDecimal ... operands) {
            BigDecimal result = operands[0];
            for (int i = 1; i < operands.length; i++) {
                result = result.subtract( operands[i] );
            }
            return result;
        }
    },

    MULTIPLY ("[\\*×⋅]", "×",2, 2, false, true, true) {
        @Override
        public  BigDecimal evaluate (BigDecimal ... operands) {
            BigDecimal result = new BigDecimal(1);
            for (BigDecimal o: operands) {
                result = result.multiply(o);
            }
            return result;
        }
    },

    DIVIDE ("[/÷]","÷",2, 2, false, true, true) {
        @Override
        public BigDecimal evaluate (BigDecimal ... operands) {
            //TODO - add logic to handle irrational and repetitive numbers
            BigDecimal result = operands[0];
            for (int i = 1; i < operands.length; i++) {
                result = result.divide(operands[i]);
            }
            return result;
        }
    },

    UNKNOWN("", null, 100_000, 0, false, false, false) {
        @Override
        public BigDecimal evaluate (BigDecimal ... operands) {
            throw new UnsupportedOperationException("Invalid Token!");
        }
    },

    MOD ("[%]", "%", 2, 2, false, true, true) {
        @Override
        public BigDecimal evaluate (BigDecimal ... operands) {
            BigDecimal result = operands[0];
            for (int i = 1; i < operands.length; i++) {
                result = result.remainder(operands[i]);
            }

            return result;
        }
    },

    LPAREN ("[(]", "(", -2, 0, false, false, false) {
        @Override
        public BigDecimal evaluate (BigDecimal ... operands) {
            throw new UnsupportedOperationException("Token cannot be evaluated!");
            //TODO - determine if LPAREN should be evaluatable
        }
    },

    RPAREN ("[)]", "(", -1, 0, false, false, false) {
        @Override
        public BigDecimal evaluate (BigDecimal ... operands) {
            throw new UnsupportedOperationException("Token cannot be evaluated");
            //TODO - determine if RPAREN should be evaluatable
        }
    },

    /* TODO - finish creating POW operation
    POW ("[\\^*{2}]", "^", 3, 2, false, true, false) {
        @Override
        public BigDecimal evaluate (BigDecimal... operands) {
            BigDecimal result = operands[0];

            for (int i = 1; i < operands.length; i++) {
                result.pow(operands[i]);
            }
        }
    }
    */
    ;




    @Getter private final String symbols;
    @Getter private final String preferredSymbol;
    @Getter private final int precedence;
    @Getter private final int operandNum;
    @Getter private final boolean containsValue;
    @Getter private final boolean evaluatable;
    @Getter private final boolean leftAssociative;

    public abstract BigDecimal evaluate (BigDecimal ... operands);

    TokenType(String symbols, String preferredSymbol, int precedence, int operandNum, boolean containsValue, boolean evaluatable, boolean
                      leftAssociative) {

        this.symbols = symbols;
        this.preferredSymbol = preferredSymbol;
        this.precedence = precedence;
        this.operandNum = operandNum;
        this.containsValue = containsValue;
        this.evaluatable = evaluatable;
        this.leftAssociative = leftAssociative;
    }
}
