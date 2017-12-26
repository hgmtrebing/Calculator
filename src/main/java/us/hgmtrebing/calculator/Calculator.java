package us.hgmtrebing.calculator;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    public static class Token {

        @Getter
        private final BigDecimal value;
        @Getter private final TokenType type;

        public Token (BigDecimal value, TokenType type) {
            this.value = value;
            this.type = type;
        }

        public Token (BigDecimal value) {
            this (value, TokenType.VALUE);
        }

        public Token (TokenType type) {
            this (new BigDecimal(0), type);
        }

        /**
         * This method takes a String and attempts to parse an individual token from it.
         * @param possibleToken
         * @return
         */
        public static Token parseToken (String possibleToken) {
            for (TokenType t : TokenType.values()) {
                if (Pattern.matches(t.getSymbols(), possibleToken)) {
                    if (t.equals(TokenType.VALUE)) {
                        return new Token(new BigDecimal(possibleToken));
                    }
                    return new Token(t);
                }
            }
            return new Token(TokenType.UNKNOWN);
        }

        /**
         * This method takes a complete expression (contained within a String) and parses a
         * List of Token objects from it
         * @param expression
         * @return
         */
        public static List<Token> tokenize (String expression) {
            StringBuilder tokenPattern = new StringBuilder();
            for (TokenType t : TokenType.values()) {
                if (t.equals(TokenType.UNKNOWN)) {
                    continue;
                }
                //\\G matches the end of the last match
                //This ensures processing is stopped when invalid characters are reached
                //Without \\G, the Matcher just skips over invalid characters and proceeds to the next match
                tokenPattern.append(String.format("|(?<%s>%s)", t.name(), "\\G" + t.getSymbols()));
            }
            expression = expression.replaceAll("\\s", ""); //eliminate whitespace
            Matcher m = Pattern.compile(tokenPattern.toString().substring(1)).matcher(expression);
            int end = 0; //used to contain the index of the last matched reached (for error-checking)
            List<Token> tokens = new ArrayList<>();
            while (m.find()) {
                tokens.add( parseToken(m.group()) );
                end = m.end(); //update end with the most recent index reached
            }
            if (end != m.regionEnd()) {
                throw new IllegalArgumentException("The expression contains illegal characters!");
            }
            return tokens;
        }

        public static Deque<Token> infixToPostfix (List<Token> infixExpression) {
            Deque<Token> postfixExpression = new ArrayDeque<>(); //Deque, treated as a Queue
            Deque<Token> tempOpStack = new ArrayDeque<>(); //Deque, treated as a Stack
            for (Token t : infixExpression) {
                //If the token is a value, it can be added immediately to the postfix expression
                if (t.getType().equals(TokenType.VALUE)) {
                    postfixExpression.addLast(t);
                } else {
                    //While the operator stack is NOT empty and has a token of greater or equal precedence...
                    while (!tempOpStack.isEmpty() &&
                            tempOpStack.peekFirst().getType().getPrecedence() >= t.getType().getPrecedence()) {
                        //Remove the operator from the stack and add it to the postfix expression
                        postfixExpression.addLast( tempOpStack.removeFirst() );
                    }
                    tempOpStack.addFirst(t);
                }
            }
            //Empty out any remaining tokens from tempOpStack to postfixExpression
            while ( !tempOpStack.isEmpty() ) {
                postfixExpression.addLast( tempOpStack.removeFirst() );
            }
            return postfixExpression;
        }

        public static Deque<Token> infixToPostfix (String infixExpression) {
            return infixToPostfix( tokenize(infixExpression) );
        }

        public static BigDecimal evaluate (Deque<Token> postfixExpression) {
            BigDecimal operand1;
            BigDecimal operand2;
            BigDecimal result;
            Deque<BigDecimal> tempStack = new ArrayDeque<>();
            while ( !postfixExpression.isEmpty() ) {
                Token t = postfixExpression.removeFirst();
                if ( t.getType().equals(TokenType.VALUE) ) {
                    tempStack.addFirst( t.getValue() );
                } else {
                    operand1 = tempStack.removeFirst();
                    operand2 = tempStack.removeFirst();
                    if (t.getType().isLeftAssociative() ) {
                        result = t.getType().evaluate(operand2, operand1);
                    } else {
                        result = t.getType().evaluate(operand1, operand2);
                    }
                    tempStack.addFirst ( result );
                }
            }
            if ( tempStack.size() > 1 || tempStack.isEmpty() ) {
                throw new IllegalStateException("Expression invalid!");
            }

            //last number on stack is the final result - strip trailing zeros
            return tempStack.removeFirst().stripTrailingZeros();
        }

        public static BigDecimal evaluate (List<Token> infixExpression) {
            Deque<Token> d = infixToPostfix ( infixExpression );
            return evaluate ( d );
        }

        public static BigDecimal evaluate ( String expression ) {
            Deque<Token> d = infixToPostfix ( expression );
            return evaluate ( d );
        }


        @Override
        public boolean equals (Object o) {
            if ( this == o) {
                return true;
            }
            if ( !(o instanceof Token)) {
                return false;
            }
            Token o2 = (Token)o;
            if (o2.getValue().equals( this.getValue() ) &&
                    o2.getType().equals( this.getType() )) {
                return true;
            }
            return false;
        }

        @Override
        public String toString () {
            if (this.getType().equals(TokenType.VALUE)) {
                return this.getValue().toString();
            }
            return this.getType().name();
        }

        public static void main(String[] args) {
            /*
            //Principally my Regex Experiments
            StringBuilder tokenPattern = new StringBuilder();
            for (TokenType t : TokenType.values()) {
                if (t.equals(TokenType.UNKNOWN)) {
                    continue;
                }
                tokenPattern.append(String.format("|(?<%s>%s)", t.name(), "\\G" + t.getSymbols()));
            }
            String tok = tokenPattern.toString().substring(1);
            Matcher m = Pattern.compile(tok).matcher("5+4*4.77--*-7");

            int end = 0;
            while (m.find()) {
                System.out.println ( m.group() );
                end = m.end();
            }

            if (end != m.regionEnd()){
                System.out.println ("NOT END");
            }
            */
            /*
            Matcher m = Pattern.compile ("\\G\\d").matcher("71902a9023j");

            while (m.find()) {
                System.out.println (m.group());

            }
            */
            System.out.println ("END TEST - HGMT");
        }
    }

    public static enum TokenType {

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
                BigDecimal result = operands[0];
                for (int i = 1; i < operands.length; i++) {
                    result = result.subtract( operands[i] );
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
                //TODO - add logic to handle irrational and repetitive numbers
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
}
