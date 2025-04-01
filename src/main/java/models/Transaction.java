package models;

import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Transaction implements Serializable {
    public enum Type {DEPOSIT, WITHDRAWAL}

    public final Type type;

    private final BigDecimal amount;

    private final LocalDateTime timestamp;

    public Transaction(Type type, BigDecimal amount) {
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }
}
