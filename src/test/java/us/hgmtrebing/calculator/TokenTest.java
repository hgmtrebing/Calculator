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
    //FYI, Assertion methods go in order of -expected, actual-

    //The first seven tests are to ensure that proper numeric tokens can be constructed ...
    // ... using the constructors provided in Token

    @Test
    public void positiveIntegerTest1 () {
        Token t = new Token(new BigDecimal(2), TokenType.VALUE);
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(2), t.getValue());
    }

    @Test
    public void positiveIntegerTest2 () {
        Token t = new Token(new BigDecimal(486));
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(486), t.getValue());
    }

    @Test
    public void zeroIntegerTest () {
        Token t = new Token(new BigDecimal(0));
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(0), t.getValue());
    }

    @Test
    public void negativeIntegerTest1 () {
        Token t = new Token(new BigDecimal(-4));
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(-4), t.getValue());
    }

    @Test
    public void negativeIntegerTest2 () {
        Token t = new Token(new BigDecimal(-3_249_331), TokenType.VALUE);
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(-3_249_331), t.getValue());
    }

    @Test
    public void positiveFloatTest () {
        Token t = new Token(new BigDecimal(7_124.9341));
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(7_124.9341), t.getValue());
    }

    @Test
    public void negativeFloatTest () {
        Token t = new Token(new BigDecimal(-47_993.621123));
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(-47_993.621123), t.getValue());
    }

    @Test
    public void operatorTokenTest1 () {
        Token t = new Token(TokenType.ADD);
        Assertions.assertEquals(TokenType.ADD, t.getType());
        Assertions.assertNotEquals(TokenType.SUBTRACT, t.getType());
    }

    @Test
    public void operatorTokenTest2 () {
        Token t = new Token(TokenType.SUBTRACT);
        Assertions.assertEquals(TokenType.SUBTRACT, t.getType());
        Assertions.assertNotEquals(TokenType.VALUE, t.getType());
    }

    @Test
    public void parseTokenTest1 () {
        Token t = Tokenizer.parseToken("-47");
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal(-47), t.getValue());
    }

    @Test
    public void parseTokenTest2 () {
        Token t = Tokenizer.parseToken("497.3920");
        Assertions.assertEquals(TokenType.VALUE, t.getType());
        Assertions.assertEquals(new BigDecimal("497.3920"), t.getValue());
    }

    @Test
    public void parseTokenTest3 () {
        Token t = Tokenizer.parseToken("+");
        Assertions.assertEquals(TokenType.ADD, t.getType());
    }

    @Test
    public void parseTokenTest4 () {
        Token t = Tokenizer.parseToken("-");
        Assertions.assertEquals(TokenType.SUBTRACT, t.getType());
    }

    @Test
    public void parseTokenTest5 () {
        Token t = Tokenizer.parseToken("*");
        Assertions.assertEquals(TokenType.MULTIPLY, t.getType());
    }

    @Test
    public void parseTokenTest6 () {
        Token t = Tokenizer.parseToken("/");
        Assertions.assertEquals(TokenType.DIVIDE, t.getType());
    }

    @Test
    public void parseTokenTest7 () {
        Token t = Tokenizer.parseToken("axb");
        Assertions.assertEquals (TokenType.UNKNOWN, t.getType());
    }

    @Test
    public void parseTokenTest8 () {
        Token t = Tokenizer.parseToken ("a642z");
        Assertions.assertEquals(TokenType.UNKNOWN, t.getType());
    }

    @Test
    public void parseTokenTest9 () {
        Token t = Tokenizer.parseToken("*47");
        Assertions.assertEquals (TokenType.UNKNOWN, t.getType());
    }

    @Test
    public void parseTokenTest10 () {
        Token t = Tokenizer.parseToken("%");
        Assertions.assertEquals(TokenType.MOD, t.getType());
    }

    @Test
    public void parseTokenTest11 () {
        Token t = Tokenizer.parseToken("(");
        Assertions.assertEquals(TokenType.LPAREN, t.getType());
    }

    @Test
    public void parseTokenTest12 () {
        Token t = Tokenizer.parseToken(")");
        Assertions.assertEquals(TokenType.RPAREN, t.getType());
    }


    @Test
    public void tokenizeTest1() {
        List <Token> t = Tokenizer.tokenize("4+8");
        List <Token> o = Arrays.asList(Tokenizer.parseToken("4"), Tokenizer.parseToken("+"), Tokenizer.parseToken("8"));
        Assertions.assertEquals(o, t);
    }

    @Test
    public void tokenizeTest2() {
        List <Token> t = Tokenizer.tokenize("-194.7*12-22+5.5");
        List <Token> o = Arrays.asList( Tokenizer.parseToken("-194.7"), Tokenizer.parseToken("*"), Tokenizer.parseToken("12"),
                                        Tokenizer.parseToken("-"), Tokenizer.parseToken("22"), Tokenizer.parseToken("+"),
                                        Tokenizer.parseToken("5.5"));
        Assertions.assertEquals(o, t);
    }

    @Test
    public void tokenizeTest3() {
        List<Token> t = Tokenizer.tokenize("-42--49.17/15.2+19.00");
        List<Token> o = Arrays.asList(Tokenizer.parseToken("-42"), Tokenizer.parseToken("-"), Tokenizer.parseToken("-49.17"),
                        Tokenizer.parseToken("/"), Tokenizer.parseToken("15.2"), Tokenizer.parseToken("+"),
                        Tokenizer.parseToken("19.00"));
        Assertions.assertEquals(o, t);
    }

    @Test
    public void tokenizeTest4() {
        try {
            Class e = Class.forName("IllegalArgumentException");
            Assertions.assertThrows(e, new Executable() {
                @Override
                public void execute() {
                    List<Token> t = Tokenizer.tokenize("abc");
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
                    List<Token> t = Tokenizer.tokenize( "c4d.74");
                    List<Token> o = Arrays.asList(new Token(new BigDecimal(4)), new Token(new BigDecimal("74")));
                    Assertions.assertEquals(o, t);
                };
            });
        } catch (Exception t) {

        }
    }

    @Test
    public void infixToPostfixTest1 () {
        Deque<Token> a = new ArrayDeque<>( Arrays.asList ( Tokenizer.parseToken("3"),
                Tokenizer.parseToken("4"), Tokenizer.parseToken("+")) );
        Deque<Token> b = Tokenizer.infixToPostfix("3+4");
        Assertions.assertEquals(a.toString(), b.toString());
    }

    @Test
    public void infixToPostfixTest2 () {
        Deque<Token> a = new ArrayDeque<>( Arrays.asList( Tokenizer.parseToken("3"),
                Tokenizer.parseToken("9"), Tokenizer.parseToken("*"), Tokenizer.parseToken("-24.772"), Tokenizer.parseToken("5"),
                Tokenizer.parseToken("/"), Tokenizer.parseToken("-")));
        Deque<Token> b = Tokenizer.infixToPostfix("3*9--24.772/5");
        Assertions.assertEquals(a.toString(), b.toString());
    }

    @Test
    public void infixToPostfixTest3 () {
        Deque<Token> a = new ArrayDeque<>( Arrays.asList( Tokenizer.parseToken("-45123.19"), Tokenizer.parseToken("27"),
                Tokenizer.parseToken("*"), Tokenizer.parseToken("4"), Tokenizer.parseToken("3"), Tokenizer.parseToken("*"),
                Tokenizer.parseToken("+")));
        Deque<Token> b = Tokenizer.infixToPostfix("-45123.19 * 27 + 4 * 3");
        Assertions.assertEquals(a.toString(), b.toString());
    }

    @Test
    public void infixToPostfixTest4 () {
        Deque<Token> a = new ArrayDeque<>(Arrays.asList(Tokenizer.parseToken("3"), Tokenizer.parseToken("2"),
                Tokenizer.parseToken("+"), Tokenizer.parseToken("5"), Tokenizer.parseToken("*"),
                Tokenizer.parseToken("4"), Tokenizer.parseToken("+")));

        Deque<Token> b = Tokenizer.infixToPostfix("(3+2)*5+4");
        Assertions.assertEquals(a.toString(), b.toString());
    }

    @Test
    public void evaluateTest1 () {
        Assertions.assertEquals(new BigDecimal("12"), Tokenizer.evaluate("7+5"));
    }

    @Test
    public void evaluateTest2 () {
        Assertions.assertEquals(new BigDecimal("12"), Tokenizer.evaluate("3*4"));
    }

    @Test
    public void evaluateTest3 () {
        Assertions.assertEquals(new BigDecimal("32"), Tokenizer.evaluate("6*5+4/2"));
    }

    @Test
    public void evaluateTest4 () {
        Assertions.assertEquals(new BigDecimal("-27.5"), Tokenizer.evaluate ("-10+40/-2+2.5"));
    }

    @Test
    public void evaluateTest5 () {
        Assertions.assertEquals(new BigDecimal("14.25"), Tokenizer.evaluate("6.125*2+2"));
    }

    @Test
    public void evaluateTest6 () {
        Assertions.assertEquals(new BigDecimal ("-94.0079"), Tokenizer.evaluate("20.0025*-3-0.0004-68/2"));
    }

    @Test
    public void evaluateTest7 () {
        Assertions.assertEquals( new BigDecimal("-1"), Tokenizer.evaluate("-60/3/2/2/5"));
    }

    @Test
    public void evaluateTest8 () {
        Assertions.assertEquals( new BigDecimal ("61.0234"), Tokenizer.evaluate("25.0078 * 3 -14"));
    }

    @Test
    public void evaluateTest9 () {
        Assertions.assertEquals( new BigDecimal("1"), Tokenizer.evaluate("1+2-3*4/5+6-7*8/10"));
    }

    @Test
    public void evaluateTest10 () {
        Assertions.assertEquals( new BigDecimal("5"), Tokenizer.evaluate("2+19 % 16"));
    }

    @Test
    public void evaluateTest11 () {
        Assertions.assertEquals( new BigDecimal("-5.7"), Tokenizer.evaluate("-8.7+7%4"));
    }

    @Test
    public void evaluateTest12 () {
        Assertions.assertEquals(new BigDecimal("28"), Tokenizer.evaluate("(3+2)*5+3"));
    }

    @Test
    public void evaluateTest13 () {
        Assertions.assertEquals(new BigDecimal ("-93.17"), Tokenizer.evaluate("(15+16) * (1-4) -.17"));
    }

    @Test
    public void evaluateTest14 () {
        Assertions.assertEquals(new BigDecimal("-8.75"), Tokenizer.evaluate("((((3+5)))*4)/(5-9)-.75"));
    }

}
