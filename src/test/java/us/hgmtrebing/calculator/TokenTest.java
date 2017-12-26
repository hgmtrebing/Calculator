package us.hgmtrebing.calculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Arrays;
import java.util.Deque;

public class TokenTest {
    //TODO - I need to reverse all of the operands in my test, for clarity
    //FYI, Assertion methods go in order of -expected, actual-

    //The first seven tests are to ensure that proper numeric tokens can be constructed ...
    // ... using the constructors provided in Token

    @Test
    public void positiveIntegerTest1 () {
        Calculator.Token t = new Calculator.Token(new BigDecimal(2), Calculator.TokenType.VALUE);
        Assertions.assertEquals(Calculator.TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(2), t.getValue());
    }

    @Test
    public void positiveIntegerTest2 () {
        Calculator.Token t = new Calculator.Token(new BigDecimal(486));
        Assertions.assertEquals(Calculator.TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(486), t.getValue());
    }

    @Test
    public void zeroIntegerTest () {
        Calculator.Token t = new Calculator.Token(new BigDecimal(0));
        Assertions.assertEquals(Calculator.TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(0), t.getValue());
    }

    @Test
    public void negativeIntegerTest1 () {
        Calculator.Token t = new Calculator.Token(new BigDecimal(-4));
        Assertions.assertEquals(Calculator.TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(-4), t.getValue());
    }

    @Test
    public void negativeIntegerTest2 () {
        Calculator.Token t = new Calculator.Token(new BigDecimal(-3_249_331), Calculator.TokenType.VALUE);
        Assertions.assertEquals(Calculator.TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(-3_249_331), t.getValue());
    }

    @Test
    public void positiveFloatTest () {
        Calculator.Token t = new Calculator.Token(new BigDecimal(7_124.9341));
        Assertions.assertEquals(Calculator.TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(7_124.9341), t.getValue());
    }

    @Test
    public void negativeFloatTest () {
        Calculator.Token t = new Calculator.Token(new BigDecimal(-47_993.621123));
        Assertions.assertEquals(Calculator.TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(-47_993.621123), t.getValue());
    }

    @Test
    public void operatorTokenTest1 () {
        Calculator.Token t = new Calculator.Token(Calculator.TokenType.ADD);
        Assertions.assertEquals(Calculator.TokenType.ADD, t.getType());
        Assertions.assertNotEquals(Calculator.TokenType.SUBTRACT, t.getType());
    }

    @Test
    public void operatorTokenTest2 () {
        Calculator.Token t = new Calculator.Token(Calculator.TokenType.SUBTRACT);
        Assertions.assertEquals(Calculator.TokenType.SUBTRACT, t.getType());
        Assertions.assertNotEquals(Calculator.TokenType.VALUE, t.getType());
    }

    @Test
    public void parseTokenTest1 () {
        Calculator.Token t = Calculator.Token.parseToken("-47");
        Assertions.assertEquals(Calculator.TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(-47), t.getValue());
    }

    @Test
    public void parseTokenTest2 () {
        Calculator.Token t = Calculator.Token.parseToken("497.3920");
        Assertions.assertEquals(Calculator.TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal("497.3920"), t.getValue());
    }

    @Test
    public void parseTokenTest3 () {
        Calculator.Token t = Calculator.Token.parseToken("+");
        Assertions.assertEquals(Calculator.TokenType.ADD, t.getType());
    }

    @Test
    public void parseTokenTest4 () {
        Calculator.Token t = Calculator.Token.parseToken("-");
        Assertions.assertEquals(Calculator.TokenType.SUBTRACT, t.getType());
    }

    @Test
    public void parseTokenTest5 () {
        Calculator.Token t = Calculator.Token.parseToken("*");
        Assertions.assertEquals(Calculator.TokenType.MULTIPLY, t.getType());
    }

    @Test
    public void parseTokenTest6 () {
        Calculator.Token t = Calculator.Token.parseToken("/");
        Assertions.assertEquals(Calculator.TokenType.DIVIDE, t.getType());
    }

    @Test
    public void parseTokenTest7 () {
        Calculator.Token t = Calculator.Token.parseToken("axb");
        Assertions.assertEquals (Calculator.TokenType.UNKNOWN, t.getType());
    }

    @Test
    public void parseTokenTest8 () {
        Calculator.Token t = Calculator.Token.parseToken ("a642z");
        Assertions.assertEquals(Calculator.TokenType.UNKNOWN, t.getType());
    }

    @Test
    public void parseTokenTest9 () {
        Calculator.Token t = Calculator.Token.parseToken("*47");
        Assertions.assertEquals (Calculator.TokenType.UNKNOWN, t.getType());
    }

    @Test
    public void tokenizeTest1() {
        List <Calculator.Token> t = Calculator.Token.tokenize("4+8");
        List <Calculator.Token> o = Arrays.asList(Calculator.Token.parseToken("4"), Calculator.Token.parseToken("+"), Calculator.Token.parseToken("8"));
        Assertions.assertEquals(o, t);
    }

    @Test
    public void tokenizeTest2() {
        List <Calculator.Token> t = Calculator.Token.tokenize("-194.7*12-22+5.5");
        List <Calculator.Token> o = Arrays.asList( Calculator.Token.parseToken("-194.7"), Calculator.Token.parseToken("*"), Calculator.Token.parseToken("12"),
                                        Calculator.Token.parseToken("-"), Calculator.Token.parseToken("22"), Calculator.Token.parseToken("+"),
                                        Calculator.Token.parseToken("5.5"));
        Assertions.assertEquals(o, t);
    }

    @Test
    public void tokenizeTest3() {
        List<Calculator.Token> t = Calculator.Token.tokenize("-42--49.17/15.2+19.00");
        List<Calculator.Token> o = Arrays.asList(Calculator.Token.parseToken("-42"), Calculator.Token.parseToken("-"), Calculator.Token.parseToken("-49.17"),
                        Calculator.Token.parseToken("/"), Calculator.Token.parseToken("15.2"), Calculator.Token.parseToken("+"),
                        Calculator.Token.parseToken("19.00"));
        Assertions.assertEquals(o, t);
    }

    @Test
    public void tokenizeTest4() {
        try {
            Class e = Class.forName("IllegalArgumentException");
            Assertions.assertThrows(e, new Executable() {
                @Override
                public void execute() {
                    List<Calculator.Token> t = Calculator.Token.tokenize("abc");
                    List<Calculator.Token> o = Arrays.asList();
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
                    List<Calculator.Token> t = Calculator.Token.tokenize( "c4d.74");
                    List<Calculator.Token> o = Arrays.asList(new Calculator.Token(new BigDecimal(4)), new Calculator.Token(new BigDecimal("74")));
                    Assertions.assertEquals(o, t);
                };
            });
        } catch (Exception t) {

        }
    }

    @Test
    public void infixToPostfixTest1 () {
        Deque<Calculator.Token> a = new ArrayDeque<>( Arrays.asList ( Calculator.Token.parseToken("3"),
                Calculator.Token.parseToken("4"), Calculator.Token.parseToken("+")) );
        Deque<Calculator.Token> b = Calculator.Token.infixToPostfix("3+4");
        Assertions.assertEquals(a.toString(), b.toString());
    }

    @Test
    public void infixToPostfixTest2 () {
        Deque<Calculator.Token> a = new ArrayDeque<>( Arrays.asList( Calculator.Token.parseToken("3"),
                Calculator.Token.parseToken("9"), Calculator.Token.parseToken("*"), Calculator.Token.parseToken("-24.772"), Calculator.Token.parseToken("5"),
                Calculator.Token.parseToken("/"), Calculator.Token.parseToken("-")));
        Deque<Calculator.Token> b = Calculator.Token.infixToPostfix("3*9--24.772/5");
        Assertions.assertEquals(a.toString(), b.toString());
    }

    @Test
    public void infixToPostfixTest3 () {
        Deque<Calculator.Token> a = new ArrayDeque<>( Arrays.asList( Calculator.Token.parseToken("-45123.19"), Calculator.Token.parseToken("27"),
                Calculator.Token.parseToken("*"), Calculator.Token.parseToken("4"), Calculator.Token.parseToken("3"), Calculator.Token.parseToken("*"),
                Calculator.Token.parseToken("+")));
        Deque<Calculator.Token> b = Calculator.Token.infixToPostfix("-45123.19 * 27 + 4 * 3");
        Assertions.assertEquals(a.toString(), b.toString());
    }

    @Test
    public void evaluateTest1 () {
        Assertions.assertEquals(new BigDecimal("12"), Calculator.Token.evaluate("7+5"));
    }

    @Test
    public void evaluateTest2 () {
        Assertions.assertEquals(new BigDecimal("12"), Calculator.Token.evaluate("3*4"));
    }

    @Test
    public void evaluateTest3 () {
        Assertions.assertEquals(new BigDecimal("32"), Calculator.Token.evaluate("6*5+4/2"));
    }

    @Test
    public void evaluateTest4 () {
        Assertions.assertEquals(new BigDecimal("-27.5"), Calculator.Token.evaluate ("-10+40/-2+2.5"));
    }

    @Test
    public void evaluateTest5 () {
        Assertions.assertEquals(new BigDecimal("14.25"), Calculator.Token.evaluate("6.125*2+2"));
    }

    @Test
    public void evaluateTest6 () {
        Assertions.assertEquals(new BigDecimal ("-94.0079"), Calculator.Token.evaluate("20.0025*-3-0.0004-68/2"));
    }

    @Test
    public void evaluateTest7 () {
        Assertions.assertEquals( new BigDecimal("-1"), Calculator.Token.evaluate("-60/3/2/2/5"));
    }

    @Test
    public void evaluateTest8 () {
        Assertions.assertEquals( new BigDecimal ("61.0234"), Calculator.Token.evaluate("25.0078 * 3 -14"));
    }

    @Test
    public void evaluateTest9 () {
        Assertions.assertEquals( new BigDecimal("1"), Calculator.Token.evaluate("1+2-3*4/5+6-7*8/10"));
    }
}
