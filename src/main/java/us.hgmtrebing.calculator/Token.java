package us.hgmtrebing.calculator;

import lombok.Getter;

import java.math.BigDecimal;

public class Token {

    @Getter private final BigDecimal value;
    @Getter private final TokenType type;

    public Token(BigDecimal value, TokenType type) {
        this.value = value;
        this.type = type;
    }

    public Token(BigDecimal value) {
        this (value, TokenType.VALUE);
    }

    public Token(TokenType type) {
        this (new BigDecimal(0), type);
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
