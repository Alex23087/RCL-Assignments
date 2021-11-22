public class Transaction{
    TransactionType type;
    long timestamp;

    public Transaction(TransactionType transactionType, long timestamp){
        this.type = transactionType;
        this.timestamp = timestamp;
    }
}