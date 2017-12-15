package us.hgmtrebing.calculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

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

}
