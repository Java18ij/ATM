import java.util.Locale;
import java.util.Scanner;
public class ATM {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in).useLocale(Locale.ENGLISH);

        Bank theBank = new Bank("Bank of Dragutin");

        User aUser = theBank.addUser("Mike", "Grin", "1234");

        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        //noinspection InfiniteLoopStatement
        while (true){
            curUser = ATM.mainMenuPrompt(theBank, sc);

            ATM.printUserMenu(curUser, sc);
        }
    }

    private static User mainMenuPrompt(Bank theBank, Scanner sc) {
        String userId;
        String pin;
        User authUser;
        do {
            System.out.printf("\n\n Welcome to %s\n\n", theBank.getName());
            System.out.print("Enter user ID: ");
            userId = sc.nextLine();
            System.out.print("Enter pin: ");
            pin = sc.nextLine();
            authUser = theBank.userLogin(userId, pin);
            if (authUser == null){
                System.out.println("Incorrect user ID/pin combination " +  "PLease try again.");

            }
        }while (authUser == null);
        return authUser;
    }
    private static void printUserMenu(User theUser, Scanner sc) {

        theUser.printAccountsSummary();
        int choice;

        do {
            System.out.printf("Welcome %s, what you would like to do?\n", theUser.getFirstName());
            System.out.println(" 1) Show the transaction history");
            System.out.println(" 2) Withdrawn");
            System.out.println(" 3) Deposit");
            System.out.println(" 4) Transfer");
            System.out.println(" 5) Quit");

            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice. Please choose 1-5");
            }
        } while (choice < 1 || choice > 5);

        switch (choice){
            case 1:
                ATM.showTransHistory(theUser, sc);
                break;
            case 2:
                ATM.withdrawnFunds(theUser, sc);
                break;
            case 3:
                ATM.depositFunds(theUser, sc);
                break;
            case 4:
                ATM.transferFunds(theUser, sc);
                break;
            case 5:
                sc.nextLine();
                break;
        }
        if (choice !=5){
            ATM.printUserMenu(theUser, sc);
        }
    }
    private static void showTransHistory(User theUser, Scanner sc) {
        int theAcct;
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" + "whose transaction you want to see: ",
                    theUser.numAccounts());
            theAcct = sc.nextInt() - 1;
            if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
                System.out.println("Invalid accounts. Please try again");
            }
        } while (theAcct < 0 || theAcct >= theUser.numAccounts());
        theUser.printAcctTransHistory(theAcct);
    }
    private static void transferFunds(User theUser, Scanner sc) {
        int fromAccount;
        int toAccount;
        double amount;
        double acctBal;
        do {
            System.out.printf("Enter the number (1 - %d) of the account" + " to transfer from: ", theUser.numAccounts());
            fromAccount = sc.nextInt() - 1;
            if (fromAccount < 0 || fromAccount >= theUser.numAccounts()){
                System.out.println("Invalid accounts. Please try again");
            }
        } while (fromAccount < 0 || fromAccount >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAccount);
        do {
            System.out.printf("Enter the number (1 - %d) of the account" + "to transfer to: ", theUser.numAccounts());
            toAccount = sc.nextInt() - 1;
            if (toAccount < 0 || toAccount >= theUser.numAccounts()){
                System.out.println("Invalid accounts. Please try again");
            }
        } while (toAccount < 0 || toAccount >= theUser.numAccounts());
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f) : $",
                    acctBal);
            amount = sc.nextDouble();
            if (amount < 0){
                System.out.println("Amount must be greater than zero.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than\n" +
                        "balance of $%.02f", acctBal);
            }
        }while (amount < 0 || amount > acctBal);

        theUser.addAcctTransaction(fromAccount, - 1 * amount, String.format(
                "Transfer to account %s", theUser.getAcctUUId(toAccount)));
        theUser.addAcctTransaction(toAccount, amount, String.format(
                "Transfer to account %s", theUser.getAcctUUId(fromAccount)));
    }
    private static void withdrawnFunds(User theUser, Scanner sc) {
        int fromAccount;
        double amount;
        double acctBal;
        String memo;
        do {
            System.out.printf("Enter the number (1 - %d) of the account" + " to withdrawn from: ", theUser.numAccounts());
            fromAccount = sc.nextInt() - 1;
            if (fromAccount < 0 || fromAccount >= theUser.numAccounts()){
                System.out.println("Invalid accounts. Please try again");
            }
        } while (fromAccount < 0 || fromAccount >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAccount);
        do {
            System.out.printf("Enter the amount to withdrawn (max $%.02f) : $",
                    acctBal);
            amount = sc.nextDouble();
            if (amount < 0){
                System.out.println("Amount must be greater than zero.");
            } else if (amount > acctBal) {
                System.out.printf("Amount must not be greater than " +
                        "balance of $%.02f\n", acctBal);
            }
        }while (amount < 0 || amount > acctBal);

        sc.nextLine();
        System.out.print("Enter a memo:");
        memo = sc.nextLine();
        theUser.addAcctTransaction(fromAccount, -1 * amount, memo);
    }
    private static void depositFunds(User theUser, Scanner sc) {
        int toAccount;
        double amount;
        double acctBal;
        String memo;
        do {
            System.out.printf("Enter the number (1 - %d) of the account" + " to deposit in: ", theUser.numAccounts());
            toAccount = sc.nextInt() - 1;
            if (toAccount < 0 || toAccount >= theUser.numAccounts()){
                System.out.println("Invalid accounts. Please try again");
            }
        } while (toAccount < 0 || toAccount >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(toAccount);
        do {
            System.out.printf("Enter the amount to deposit (max $%.02f) : $",
                    acctBal);
            amount = sc.nextDouble();
            if (amount < 0){
                System.out.println("Amount must be greater than zero.");
            }
        }while (amount < 0);

        sc.nextLine();
        System.out.println("Enter a memo:");
        memo = sc.nextLine();
        theUser.addAcctTransaction(toAccount, amount, memo);
    }
}