package us.hgmtrebing.calculator;

import lombok.Getter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Token {

    @Getter private final BigDecimal value;
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
                    return new Token (new BigDecimal(possibleToken));
                }
                return new Token (t);
            }
        }
        return new Token (TokenType.UNKNOWN);
    }

    public static List<Token> tokenize (String expression) {
        StringBuilder tokenPattern = new StringBuilder();
        for (TokenType t : TokenType.values()) {
            if (t.equals(TokenType.UNKNOWN)) {
                continue;
            }
            tokenPattern.append(String.format("|(?<%s>%s)", t.name(), t.getSymbols()));
        }
        Matcher m = Pattern.compile(tokenPattern.toString().substring(1)).matcher(expression);
        List<Token> tokens = new ArrayList<>();
        while (m.find()) {
            tokens.add( parseToken(m.group()) );
        }
        return tokens;
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
}


