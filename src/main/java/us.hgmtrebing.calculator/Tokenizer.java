package us.hgmtrebing.calculator;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    /**
     * This method takes a String and attempts to parse an individual token from it.
     * @param possibleToken
     * @return
     */
    public static Token parseToken(String possibleToken) {
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
    public static List<Token> tokenize(String expression) {

        //This section creates tokenPattern, which is a regex used to identify tokens
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

    public static Deque<Token> infixToPostfix(List<Token> infixExpression) {
        Deque<Token> postfixExpression = new ArrayDeque<>(); //Deque, treated as a Queue
        Deque<Token> tempOpStack = new ArrayDeque<>(); //Deque, treated as a Stack
        for (Token t : infixExpression) {
            //If the token is a value, it can be added immediately to the postfix expression
            if (t.getType().equals(TokenType.VALUE)) {
                postfixExpression.addLast(t);

                //Custom line for handling Right Parenthesis
            } else if (t.getType().equals(TokenType.RPAREN)) {

                while (!tempOpStack.isEmpty()) {
                    Token o = tempOpStack.removeFirst();
                    if (o.getType().equals(TokenType.LPAREN)) {
                        break;
                    }
                    postfixExpression.addLast (o);
                }

            } else if (t.getType().equals(TokenType.LPAREN)) {

                tempOpStack.addFirst(t);

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

    public static Deque<Token> infixToPostfix(String infixExpression) {
        return infixToPostfix( tokenize(infixExpression) );
    }

    public static BigDecimal evaluate(Deque<Token> postfixExpression) {
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

    public static BigDecimal evaluate(List<Token> infixExpression) {
        Deque<Token> d = infixToPostfix ( infixExpression );
        return evaluate ( d );
    }

    public static BigDecimal evaluate(String expression) {
        Deque<Token> d = infixToPostfix ( expression );
        return evaluate ( d );
    }
}
