package us.hgmtrebing.calculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.Deque;

public class TokenTest {
    //TODO - I need to reverse all of the operands in my test, for clarity
    //FYI, Assertion methods go in order of -expected, actual-

    //The first seven tests are to ensure that proper numeric tokens can be constructed ...
    // ... using the constructors provided in Token

    @Test
    public void positiveIntegerTest1 () {
        Token t = new Token (new BigDecimal(2), TokenType.VALUE);
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(2), t.getValue());
    }

    @Test
    public void positiveIntegerTest2 () {
        Token t = new Token (new BigDecimal(486));
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(486), t.getValue());
    }

    @Test
    public void zeroIntegerTest () {
        Token t = new Token (new BigDecimal(0));
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(0), t.getValue());
    }

    @Test
    public void negativeIntegerTest1 () {
        Token t = new Token (new BigDecimal(-4));
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(-4), t.getValue());
    }

    @Test
    public void negativeIntegerTest2 () {
        Token t = new Token (new BigDecimal(-3_249_331), TokenType.VALUE);
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(-3_249_331), t.getValue());
    }

    @Test
    public void positiveFloatTest () {
        Token t = new Token (new BigDecimal(7_124.9341));
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(7_124.9341), t.getValue());
    }

    @Test
    public void negativeFloatTest () {
        Token t = new Token (new BigDecimal(-47_993.621123));
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(-47_993.621123), t.getValue());
    }

    @Test
    public void operatorTokenTest1 () {
        Token t = new Token (TokenType.ADD);
        Assertions.assertEquals(TokenType.ADD, t.getType());
        Assertions.assertNotEquals(TokenType.SUBTRACT, t.getType());
    }

    @Test
    public void operatorTokenTest2 () {
        Token t = new Token (TokenType.SUBTRACT);
        Assertions.assertEquals(TokenType.SUBTRACT, t.getType());
        Assertions.assertNotEquals(TokenType.VALUE, t.getType());
    }

    @Test
    public void parseTokenTest1 () {
        Token t = Token.parseToken("-47");
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(-47), t.getValue());
    }

    @Test
    public void parseTokenTest2 () {
        Token t = Token.parseToken("497.3920");
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal("497.3920"), t.getValue());
    }

    @Test
    public void parseTokenTest3 () {
        Token t = Token.parseToken("+");
        Assertions.assertEquals(TokenType.ADD, t.getType());
    }

    @Test
    public void parseTokenTest4 () {
        Token t = Token.parseToken("-");
        Assertions.assertEquals(TokenType.SUBTRACT, t.getType());
    }

    @Test
    public void parseTokenTest5 () {
        Token t = Token.parseToken("*");
        Assertions.assertEquals(TokenType.MULTIPLY, t.getType());
    }

    @Test
    public void parseTokenTest6 () {
        Token t = Token.parseToken("/");
        Assertions.assertEquals(TokenType.DIVIDE, t.getType());
    }

    @Test
    public void parseTokenTest7 () {
        Token t = Token.parseToken("axb");
        Assertions.assertEquals (TokenType.UNKNOWN, t.getType());
    }

    @Test
    public void parseTokenTest8 () {
        Token t = Token.parseToken ("a642z");
        Assertions.assertEquals(TokenType.UNKNOWN, t.getType());
    }

    @Test
    public void parseTokenTest9 () {
        Token t = Token.parseToken("*47");
        Assertions.assertEquals (TokenType.UNKNOWN, t.getType());
    }

    @Test
    public void tokenizeTest1() {
        List <Token> t = Token.tokenize("4+8");
        List <Token> o = Arrays.asList(Token.parseToken("4"), Token.parseToken("+"), Token.parseToken("8"));
        Assertions.assertEquals(o, t);
    }

    @Test
    public void tokenizeTest2() {
        List <Token> t = Token.tokenize("-194.7*12-22+5.5");
        List <Token> o = Arrays.asList( Token.parseToken("-194.7"), Token.parseToken("*"), Token.parseToken("12"),
                                        Token.parseToken("-"), Token.parseToken("22"), Token.parseToken("+"),
                                        Token.parseToken("5.5"));
        Assertions.assertEquals(o, t);
    }

    @Test
    public void tokenizeTest3() {
        List<Token> t = Token.tokenize("-42--49.17/15.2+19.00");
        List<Token> o = Arrays.asList(Token.parseToken("-42"), Token.parseToken("-"), Token.parseToken("-49.17"),
                        Token.parseToken("/"), Token.parseToken("15.2"), Token.parseToken("+"),
                        Token.parseToken("19.00"));
        Assertions.assertEquals(o, t);
    }

    @Test
    public void tokenizeTest4() {
        try {
            Class e = Class.forName("IllegalArgumentException");
            Assertions.assertThrows(e, new Executable() {
                @Override
                public void execute() {
                    List<Token> t = Token.tokenize("abc");
                    List<Token> o = Arrays.asList();
                    Assertions.assertEquals(o, t);
                };
            });
        } catch (Exception t) {

        }

    }

    @Test
    public void tokenizeTest5() {
        try {
            Class e = Class.forName("IllegalArgumentException");
            Assertions.assertThrows(e, new Executable() {
                @Override
                public void execute() {
                    List<Token> t = Token.tokenize( "c4d.74");
                    List<Token> o = Arrays.asList(new Token(new BigDecimal(4)), new Token(new BigDecimal("74")));
                    Assertions.assertEquals(o, t);
                };
            });
        } catch (Exception t) {

        }
    }

    @Test
    public void infixToPostfixTest1 () {
        Deque<Token> a = new ArrayDeque<>( Arrays.asList ( Token.parseToken("3"),
                Token.parseToken("4"), Token.parseToken("+")) );
        Deque<Token> b = Token.infixToPostfix("3+4");
        Assertions.assertEquals(a.toString(), b.toString());
    }

    @Test
    public void infixToPostfixTest2 () {
        Deque<Token> a = new ArrayDeque<>( Arrays.asList( Token.parseToken("3"),
                Token.parseToken("9"), Token.parseToken("*"), Token.parseToken("-24.772"), Token.parseToken("5"),
                Token.parseToken("/"), Token.parseToken("-")));
        Deque<Token> b = Token.infixToPostfix("3*9--24.772/5");
        Assertions.assertEquals(a.toString(), b.toString());
    }

    @Test
    public void infixToPostfixTest3 () {
        Deque<Token> a = new ArrayDeque<>( Arrays.asList( Token.parseToken("-45123.19"), Token.parseToken("27"),
                Token.parseToken("*"), Token.parseToken("4"), Token.parseToken("3"), Token.parseToken("*"),
                Token.parseToken("+")));
        Deque<Token> b = Token.infixToPostfix("-45123.19 * 27 + 4 * 3");
        Assertions.assertEquals(a.toString(), b.toString());
    }

    @Test
    public void evaluateTest1 () {
        Assertions.assertEquals(new BigDecimal("12"), Token.evaluate("7+5"));
    }

    @Test
    public void evaluateTest2 () {
        Assertions.assertEquals(new BigDecimal("12"), Token.evaluate("3*4"));
    }

    @Test
    public void evaluateTest3 () {
        Assertions.assertEquals(new BigDecimal("32"), Token.evaluate("6*5+4/2"));
    }

    @Test
    public void evaluateTest4 () {
        Assertions.assertEquals(new BigDecimal("-27.5"), Token.evaluate ("-10+40/-2+2.5"));
    }

    @Test
    public void evaluateTest5 () {
        Assertions.assertEquals(new BigDecimal("14.25"), Token.evaluate("6.125*2+2"));
    }

    @Test
    public void evaluateTest6 () {
        Assertions.assertEquals(new BigDecimal ("-94.0079"), Token.evaluate("20.0025*-3-0.0004-68/2"));
    }

    @Test
    public void evaluateTest7 () {
        Assertions.assertEquals( new BigDecimal("-1"), Token.evaluate("-60/3/2/2/5"));
    }

    @Test
    public void evaluateTest8 () {
        Assertions.assertEquals( new BigDecimal ("61.0234"), Token.evaluate("25.0078 * 3 -14"));
    }

    @Test
    public void evaluateTest9 () {
        Assertions.assertEquals( new BigDecimal("1"), Token.evaluate("1+2-3*4/5+6-7*8/10"));
    }
}
