import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class WaitingRoomChecker implements Runnable{
	int k;
	ThreadPoolExecutor pool;
	LinkedBlockingQueue<PersonTask> waitingRoom;

	public WaitingRoomChecker(int k, ThreadPoolExecutor pool, LinkedBlockingQueue<PersonTask> waitingRoom){
		this.k = k;
		this.pool = pool;
		this.waitingRoom = waitingRoom;
	}

	@Override
	public void run() {
		while(true){
			//If there's no one in the smaller waiting room and there are at least k people in the bigger one
			// then send a group of k people to the smaller waiting room
			if(pool.getActiveCount() == 0 && waitingRoom.size() >= k){
				System.out.println("Sending new batch of people");
				for(int i = 0; i < k; i++){
					pool.execute(waitingRoom.remove());
				}
			}
			try {
				//Active polling as there is no callback for the pool's active count and the queue's size
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("WaitingRoomChecker thread has been interrupted");
				return;
			}
		}
	}
}