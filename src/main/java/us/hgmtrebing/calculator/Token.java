package us.hgmtrebing.calculator;

import lombok.Getter;
import java.math.BigDecimal;

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

        try {
            BigDecimal bd = new BigDecimal(possibleToken);
            return new Token(bd);
        } catch (NumberFormatException e) {
            for (TokenType t : TokenType.values()) {
                if (t.containsSymbol (possibleToken)) {
                    return new Token (t);
                }
            }
        }

        //TODO - determine if its best to return null
        return null;
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

        return this.getValue().toString();
    }
}


