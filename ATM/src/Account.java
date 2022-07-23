import java.util.ArrayList;

public class Account {
    public double getBalance;
    private String name;
    private String uuid;
    private User holder;
    private ArrayList<Transaction> transactions;

    public Account(String name, User holder, Bank theBank){
        this.name = name;
        this.holder = holder;

        this.uuid = theBank.getNewAccountUUID();
        this.transactions = new ArrayList<Transaction>();
    }

    public String getUUId() {
        return uuid;
    }


    public String getSummaryLines() {
        double balance = this.getBalance();
        if (balance >= 0){
            return String.format("%s : $%.02f : %s", this.uuid, balance, this.name);
        } else {
            return String.format("%s : $(%.02f) : %s", this.uuid, balance, this.name);
        }
    }

    double getBalance() {
        double balance = 0;
        for (Transaction t : this.transactions){
            balance  += t.getAmount();
        }
        return balance;
    }

    public void printTransHistory() {
        System.out.printf("\nTransaction history for the account: %s", this.uuid);
        for (int k = this.transactions.size()-1; k >=0; k--){
            System.out.println(this.transactions.get(k).getSummaryLine());
        }
        System.out.println();
    }

    public void addTransaction(double amount, String memo) {
        Transaction newTrans = new Transaction(amount, memo, this);
        this.transactions.add(newTrans);
    }
}