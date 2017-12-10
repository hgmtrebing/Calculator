package us.hgmtrebing.calculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;

public class TokenTest {
    //TODO - I need to reverse all of the operands in my test, for clarity
    //FYI, Assertion methods go in order of -expected, actual-

    //The first seven tests are to ensure that proper numeric tokens can be constructed ...
    // ... using the constructors provided in Token

    @Test
    public void positiveIntegerTest1 () {
        Token t = new Token (new BigDecimal(2), TokenType.VALUE);
        Assertions.assertEquals(t.getType(), TokenType.VALUE);
        Assertions.assertEquals(t.getValue(), new BigDecimal(2));
    }

    @Test
    public void positiveIntegerTest2 () {
        Token t = new Token (new BigDecimal(486));
        Assertions.assertEquals(t.getType(), TokenType.VALUE);
        Assertions.assertEquals(t.getValue(), new BigDecimal(486));
    }

    @Test
    public void zeroIntegerTest () {
        Token t = new Token (new BigDecimal(0));
        Assertions.assertEquals(t.getType(), TokenType.VALUE);
        Assertions.assertEquals(t.getValue(), new BigDecimal(0));
    }

    @Test
    public void negativeIntegerTest1 () {
        Token t = new Token (new BigDecimal(-4));
        Assertions.assertEquals(t.getType(), TokenType.VALUE);
        Assertions.assertEquals(t.getValue(), new BigDecimal(-4));
    }

    @Test
    public void negativeIntegerTest2 () {
        Token t = new Token (new BigDecimal(-3_249_331), TokenType.VALUE);
        Assertions.assertEquals(t.getType(), TokenType.VALUE);
        Assertions.assertEquals(t.getValue(), new BigDecimal(-3_249_331));
    }

    @Test
    public void positiveFloatTest () {
        Token t = new Token (new BigDecimal(7_124.9341));
        Assertions.assertEquals(t.getType(), TokenType.VALUE);
        Assertions.assertEquals(t.getValue(), new BigDecimal(7_124.9341));
    }

    @Test
    public void negativeFloatTest () {
        Token t = new Token (new BigDecimal(-47_993.621123));
        Assertions.assertEquals(t.getType(), TokenType.VALUE);
        Assertions.assertEquals(t.getValue(), new BigDecimal(-47_993.621123));
    }

    @Test
    public void operatorTokenTest1 () {
        Token t = new Token (TokenType.ADD);
        Assertions.assertEquals(t.getType(), TokenType.ADD);
        Assertions.assertNotEquals(t.getType(), TokenType.SUBTRACT);
    }

    @Test
    public void operatorTokenTest2 () {
        Token t = new Token (TokenType.SUBTRACT);
        Assertions.assertEquals(t.getType(), TokenType.SUBTRACT);
        Assertions.assertNotEquals(t.getType(), TokenType.VALUE);
    }

    @Test
    public void parseTokenTest1 () {
        Token t = Token.parseToken("-47");
        Assertions.assertEquals(t.getType(), TokenType.VALUE);
        Assertions.assertEquals(t.getValue(), new BigDecimal(-47));
    }

    @Test
    public void parseTokenTest2 () {
        Token t = Token.parseToken("497.3920");
        Assertions.assertEquals(t.getType(), TokenType.VALUE);
        Assertions.assertEquals(t.getValue(), new BigDecimal("497.3920"));
    }

}
