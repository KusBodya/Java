package ru.Bodyaaaa;

import commads.DepositCommand;
import commads.WithdrawalCommand;
import exeptions.*;
import models.Transaction;
import models.UserAccount;
import services.BankService;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class Main {
    private static final String USERS_FILE_PATH =
            "lab-1" + File.separator +
                    "src" + File.separator +
                    "main" + File.separator +
                    "resources" + File.separator +
                    "data" + File.separator +
                    "users.dat";

    private static final String FIRST_PAGE = """
             1> Login
             2> Create an account
             3> Exit the program
            """;
    private static final String SECOND_PAGE = """
            >0 View balance
            >1 Put money into the account
            >2 Withdraw money from an account
            >3 View transactions
            >4 Exit from account
            """;
    private static BankService bankService = new BankService();


    public static void main(String[] args) {

        try {
            loadUsers();
        } catch (UserDataLoadException e) {
            System.out.println(e.getMessage());
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
        Scanner scanner = new Scanner(System.in);
        int choice;
        UserAccount currentUser = null;


        while (true) {

            if (currentUser == null) {
                System.out.println(FIRST_PAGE);
            } else {
                System.out.println(SECOND_PAGE);
            }

            String input = scanner.nextLine();

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                choice = -1;
            }

            if (currentUser == null) {
                switch (choice) {
                    // LOGIN
                    case 1: {
                        System.out.println("Enter First Name and Last Name \n Example : (Bodya)");
                        String name = scanner.nextLine();
                        System.out.println("Enter Password");
                        String password = scanner.nextLine();

                        try {
                            currentUser = bankService.loginAccount(name, password);
                        } catch (AccountNotFound e) {
                            System.out.println("Account not found");
                            currentUser = null;
                        }

                        if (currentUser != null) {
                            System.out.println("Welcome " + currentUser.getUserName());
                        }
                        break;
                    }
                    // CREATE ACCOUNT
                    case 2: {
                        System.out.println("Enter User name \n Example : (Bodya)");
                        String name = scanner.nextLine();
                        System.out.println("Enter Password");
                        String password = scanner.nextLine();
                        try {
                            currentUser = bankService.createAccount(name, password);
                            System.out.println("Account created successfully.\n");
                        } catch (UserInvalidName | AnAccountWithThisNameExists e) {
                            System.out.println("Account invalid name\n");
                        }

                        if (currentUser != null) {
                            System.out.println("Welcome " + currentUser.getUserName());
                        }

                        break;
                    }

                    case 3: {
                        try {
                            saveUsers();
                        } catch (UserDataSaveException e) {
                            System.out.println(e.getMessage());
                        }
                        System.out.println("Program will exit now.\n");
                        return;
                    }
                    default: {
                        System.out.println("Invalid choice");
                        break;
                    }
                }
            } else {
                switch (choice) {
                    // balance
                    case 0: {
                        System.out.println("Your balance : " + currentUser.getWallet().getBalance() + " rubles \n");
                        break;
                    }
                    // Dep
                    case 1: {
                        System.out.println("How much do you want to deposit?\n");
                        BigDecimal deposit = scanner.nextBigDecimal();
                        scanner.nextLine();
                        Transaction transaction = new Transaction(Transaction.Type.DEPOSIT, deposit);
                        DepositCommand depositCommand = new DepositCommand(transaction, currentUser.getWallet());

                        try {
                            depositCommand.execute();
                            System.out.println("The money has been credited to your account.");
                        } catch (InvalidAmount e) {
                            System.out.println("Invalid amount");

                        }

                        break;
                    }
                    // Withdraw
                    case 2: {

                        System.out.println("How much do you want to withdraw?\n");
                        BigDecimal withdraw = scanner.nextBigDecimal();
                        scanner.nextLine();
                        Transaction transaction = new Transaction(Transaction.Type.WITHDRAWAL, withdraw);
                        WithdrawalCommand withdrawalCommand = new WithdrawalCommand(transaction, currentUser.getWallet());
                        try {
                            withdrawalCommand.execute();
                            System.out.println("Money has been successfully withdrawn from your account.");
                        } catch (InvalidAmount e) {
                            System.out.println("Invalid amount");
                        } catch (InsufficientFunds e) {
                            System.out.println("Insufficient funds");
                        }
                        break;
                    }
                    // View transactions
                    case 3: {
                        currentUser.getWallet().viewTransactions();
                        break;
                    }
                    // exit
                    case 4: {
                        System.out.println("Goodbye " + currentUser.getUserName() + "\n");
                        currentUser = null;
                        break;
                    }

                    default: {
                        System.out.println("Invalid choice");
                        break;
                    }

                }
            }

        }


    }

    private static void saveUsers() throws UserDataSaveException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE_PATH))) {
            oos.writeObject(bankService.getUserAccountList());
            System.out.println("Данные пользователей сохранены.");
        } catch (IOException e) {
            throw new UserDataSaveException("Ошибка сохранения данных пользователей");
        }
    }

    private static void loadUsers() throws UserDataLoadException, InvalidDataException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE_PATH))) {
            List<?> accountList = (List<?>) ois.readObject();
            List<UserAccount> validatedList = new ArrayList<>();

            for (Object entry : accountList) {
                if (entry instanceof UserAccount) {
                    validatedList.add((UserAccount) entry);
                } else {
                    throw new InvalidDataException("Некорректные данные в файле.");
                }
            }
            bankService.setUserAccountList(validatedList);
            System.out.println("Данные пользователей загружены.");
        } catch (FileNotFoundException e) {
            throw new UserDataLoadException("Файл данных пользователей не найден");
        } catch (IOException | ClassNotFoundException e) {
            throw new UserDataLoadException("Ошибка загрузки данных пользователей");
        }
    }
}