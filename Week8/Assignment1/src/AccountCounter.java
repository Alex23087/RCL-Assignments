public class AccountCounter implements Runnable{
    private final Account account;
    private final int[] counters;

    public AccountCounter(Account account){
        this.account = account;
        this.counters = new int[Main.counters.length];
    }

    @Override
    public void run() {
        account.transactions.forEach((t)->counters[t.type.ordinal()]++);
        synchronized (Main.counters){
            for(int i = 0; i < counters.length; i++){
                Main.counters[i] += counters[i];
            }
        }
    }
}