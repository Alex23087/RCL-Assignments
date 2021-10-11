import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Tutor {
	public final int computerCount = 20;

	private final Condition professorCondition;
	private final Condition thesistCondition;
	private final Condition studentCondition;

	private final ReentrantLock lock;
	private final Boolean[] computers = new Boolean[computerCount];

	public Tutor() {
		lock = new ReentrantLock();
		professorCondition = lock.newCondition();
		thesistCondition = lock.newCondition();
		studentCondition = lock.newCondition();
		for (int i = 0; i < computerCount; i++) {
			computers[i] = false;
		}
	}

	public void enqueue(User user) {
		try {
			lock.lock();
			try {
				if (user instanceof Professor) {
					while (!isLabFree()) { //Wait for the lab to be free
						professorCondition.await();
					}
					setAllLabOccupied();
					lock.unlock();
					user.doTask();
					lock.lock();
					setAllLabFree();
				} else if (user instanceof Thesist) {
					int computerNeeded = ((Thesist) user).computerNeeded;
					while (lock.hasWaiters(professorCondition) || !isComputerFree(computerNeeded)) { //Wait for the professors to finish and for the computer to be free
						thesistCondition.await();
					}
					setComputerOccupied(computerNeeded);
					lock.unlock();
					user.doTask();
					lock.lock();
					setComputerFree(computerNeeded);
				} else if (user instanceof Student) {
					while (lock.hasWaiters(professorCondition) || lock.hasWaiters(thesistCondition) || isLabOccupied()) { //Wait for professors and thesists to finish and for a computer to be free
						studentCondition.await();
					}
					int computerIndex = getFreeComputer();
					setComputerOccupied(computerIndex);
					lock.unlock();
					user.doTask();
					lock.lock();
					setComputerFree(computerIndex);
				}
				signalNext(); //Signal the other waiting users
			} catch (InterruptedException ie) {
				System.out.println("Thread interrupted");
			}
		} finally {
			lock.unlock();
		}
	}

	private void signalNext() { //Signals users implementing the priority policy specified (professors first, then thesists, then students)
		if (lock.hasWaiters(professorCondition)) {
			professorCondition.signal();
		} else if (lock.hasWaiters(thesistCondition)) {
			thesistCondition.signalAll();
		} else if (lock.hasWaiters(studentCondition)) {
			studentCondition.signalAll();
		}
	}

	private boolean isLabFree() {
		for (boolean b : computers) {
			if (b) {
				return false;
			}
		}
		return true;
	}

	private boolean isLabOccupied() {
		for (boolean b : computers) {
			if (!b) {
				return false;
			}
		}
		return true;
	}

	private void setAllLabOccupied() {
		for (int i = 0; i < computerCount; i++) {
			computers[i] = true;
		}
	}

	private void setAllLabFree() {
		for (int i = 0; i < computerCount; i++) {
			computers[i] = false;
		}
	}

	private boolean isComputerFree(int index) {
		return !computers[index];
	}

	private void setComputerOccupied(int index) {
		computers[index] = true;
	}

	private void setComputerFree(int index) {
		computers[index] = false;
	}

	private int getFreeComputer() {
		for (int i = 0; i < computerCount; i++) {
			if (isComputerFree(i)) {
				return i;
			}
		}
		throw new RuntimeException("No free computers found");
	}
}