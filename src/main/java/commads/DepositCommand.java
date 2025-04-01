package commads;

import exeptions.InvalidAmount;
import lombok.RequiredArgsConstructor;
import models.Transaction;
import models.Wallet;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class DepositCommand implements TransactionCommand {

    private final Transaction transaction;

    private final Wallet wallet;

    @Override
    public void execute() throws InvalidAmount {
        BigDecimal amount = transaction.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new InvalidAmount("Invalid deposit amount");
        wallet.updateBalance(transaction);
    }

}