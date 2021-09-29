import java.util.concurrent.*;

public class Main {
    static final long KEEPALIVE_TIME = 1500;

    public static void main(String[] args) {
        if(args.length < 1){
            System.err.println("Invalid number of arguments");
            return;
        }
        int k;
        try{
            k = Integer.parseInt(args[0]);
        }catch(NumberFormatException nfe){
            System.err.println("Invalid number passed as parameter");
            return;
        }

        //Initializing thread pool. The keepalive time will make it so the operator will close the desk if no one goes there, the queue is of size k so that only k people can enter
        ThreadPoolExecutor pool = new ThreadPoolExecutor(0, 4, KEEPALIVE_TIME, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(k));

        //Initializing a linkedblockingqueue with no set size that will model the bigger waiting room
        LinkedBlockingQueue<PersonTask> waitingRoom = new LinkedBlockingQueue<>();

        //Initializing and starting the thread that will allow a group of k people to enter the smaller waiting room
        Thread waitingRoomCheckerThread = new Thread(new WaitingRoomChecker(k, pool, waitingRoom));
        waitingRoomCheckerThread.start();

        int currentPersonId = 0;
        while(true){ //Every 500ms a new person comes into the office and is added to the waiting room
            waitingRoom.add(new PersonTask(currentPersonId));
            System.out.println("Person " + currentPersonId++ + " arrived in the waiting room");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted");
            }
        }
    }
}