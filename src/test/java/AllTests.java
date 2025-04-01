import commads.DepositCommand;
import commads.WithdrawalCommand;
import exeptions.*;
import models.Transaction;
import models.UserAccount;
import models.Wallet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import services.BankService;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AllTests {
    @Nested
    class BankServiceTest {
        BankService bankService = new BankService();

        @Test
        void createAccount_validData_shouldCreate() throws UserInvalidName, AnAccountWithThisNameExists {
            UserAccount account = bankService.createAccount("TestUser", "pass123");
            assertNotNull(account);
            assertEquals("TestUser", account.getUserName());
        }

        @Test
        void createAccount_duplicateName_shouldThrowException() {
            assertThrows(AnAccountWithThisNameExists.class, () -> {
                bankService.createAccount("DuplicateUser", "pass");
                bankService.createAccount("DuplicateUser", "pass");
            });
        }

        @Test
        void loginAccount_validCredentials_shouldReturnUser() throws UserInvalidName, AnAccountWithThisNameExists, AccountNotFound {
            bankService.createAccount("AuthUser", "authpass");
            UserAccount user = bankService.loginAccount("AuthUser", "authpass");
            assertNotNull(user);
        }


    }

    @Nested
    class InputValidationTest {
        @Test
        void parseMenuChoice_invalidInput_shouldReturnDefault() {
            String input = "invalid";
            assertThrows(NumberFormatException.class, () -> Integer.parseInt(input));
        }
    }

    @Nested
    class WalletTest {
        @Test
        void deposit_validAmount_shouldUpdateBalance() throws InvalidAmount {
            Wallet wallet = new Wallet();
            Transaction transaction = new Transaction(Transaction.Type.DEPOSIT, BigDecimal.valueOf(100));
            new DepositCommand(transaction, wallet).execute();
            assertEquals(BigDecimal.valueOf(100), wallet.getBalance());
        }

        @Test
        void withdraw_insufficientFunds_shouldThrowException() {
            Wallet wallet = new Wallet();
            Transaction transaction = new Transaction(Transaction.Type.WITHDRAWAL, BigDecimal.valueOf(50));
            assertThrows(InsufficientFunds.class, () ->
                    new WithdrawalCommand(transaction, wallet).execute());
        }
    }

    @Nested
    class PersistenceTest {
        @TempDir
        Path tempDir;

        @Test
        void saveAndLoadUsers_shouldMaintainDataIntegrity() throws IOException, AnAccountWithThisNameExists {
            BankService service = new BankService();
            Path testFile = tempDir.resolve("testusers.dat");
            service.createAccount("FileUser", "filepass");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(testFile.toFile()))) {
                oos.writeObject(service.getUserAccountList());
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(testFile.toFile()))) {
                List<UserAccount> loaded = (List<UserAccount>) ois.readObject();
                assertEquals(1, loaded.size());
                assertEquals("FileUser", loaded.get(0).getUserName());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
