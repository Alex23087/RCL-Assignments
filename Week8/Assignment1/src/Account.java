import java.util.ArrayList;
import java.util.List;

public class Account{
    String owner;
    List<Transaction> transactions;

    public Account(String owner){
        this.owner = owner;
        this.transactions = new ArrayList<>();
    }
}