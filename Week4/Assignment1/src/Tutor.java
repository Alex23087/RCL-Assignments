import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tutor {
	public final int computerCount = 20;

	private final Boolean[] computers = new Boolean[computerCount];

	private final List<User> professorsWaiting = Collections.synchronizedList(new ArrayList<>());
	private final List<User> thesistsWaiting = Collections.synchronizedList(new ArrayList<>());
	private final List<User> studentsWaiting = Collections.synchronizedList(new ArrayList<>());

	public Tutor() {
		for (int i = 0; i < computerCount; i++) {
			computers[i] = false;
		}
	}

	public void enqueue(User user) {
		synchronized (this) {
			try {
				if (user instanceof Professor) {
					boolean added = false;
					while (!isLabFree()) { //Wait for the lab to be free. Serve first professor only
						if (!added) {
							professorsWaiting.add(user);
							added = true;
						}
						wait();
					}
					professorsWaiting.remove(user);
					setAllLabOccupied();
				} else if (user instanceof Thesist) {
					int computerNeeded = ((Thesist) user).computerNeeded;
					boolean added = false;
					while (professorsWaiting.size() > 0 || !isComputerFree(computerNeeded)) { //Wait for the professors to finish and for the computer to be free
						if (!added) {
							thesistsWaiting.add(user);
							added = true;
						}
						wait();
					}
					thesistsWaiting.remove(user);
					setComputerOccupied(computerNeeded);
				} else if (user instanceof Student) {
					boolean added = false;
					while (professorsWaiting.size() > 0 || thesistsWaiting.size() > 0 || isLabOccupied()) { //Wait for professors and thesists to finish and for a computer to be free
						if (!added) {
							studentsWaiting.add(user);
							added = true;
						}
						wait();
					}
					studentsWaiting.remove(user);
					int computerIndex = getFreeComputer();
					((Student) user).computerInUse = computerIndex;
					setComputerOccupied(computerIndex);
				}
			} catch (InterruptedException ie) {
				System.out.println("Thread interrupted");
			}
		}

		user.doTask();

		synchronized (this) {
			if (user instanceof Professor) {
				setAllLabFree();
			} else if (user instanceof Thesist) {
				setComputerFree(((Thesist) user).computerNeeded);
			} else if (user instanceof Student) {
				setComputerFree(((Student) user).computerInUse);
			}
			notifyAll(); //Notify the other waiting users
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