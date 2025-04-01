package models;

import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * Снятие денег со счета
 * balance сумма для снятия
 */
public class Wallet implements Serializable {

    private final List<Transaction> transactions = new ArrayList<>();
    @Getter
    private BigDecimal balance = BigDecimal.ZERO;

    public void updateBalance(Transaction transaction) {
        if (transaction.type == Transaction.Type.WITHDRAWAL) {
            balance = balance.subtract(transaction.getAmount());
        } else {
            balance = balance.add(transaction.getAmount());
        }
        transactions.add(transaction);
    }

    public void viewTransactions() {
        for (Transaction transaction : transactions) {
            System.out.println(transaction.type + " " + transaction.getAmount());
        }
    }

}
