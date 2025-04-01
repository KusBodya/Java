package commads;

import exeptions.InsufficientFunds;
import exeptions.InvalidAmount;
import lombok.RequiredArgsConstructor;
import models.Transaction;
import models.Wallet;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class WithdrawalCommand implements TransactionCommand {
    private final Transaction transaction;

    private final Wallet wallet;


    @Override
    public void execute() throws InvalidAmount, InsufficientFunds {
        BigDecimal amount = transaction.getAmount();
        BigDecimal balance = wallet.getBalance();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmount("The amount to be withdrawn must be greater than zero.");
        }

        if (amount.compareTo(balance) > 0) {
            throw new InsufficientFunds("There are not enough funds in the account.");
        }

        wallet.updateBalance(transaction);
    }
}
