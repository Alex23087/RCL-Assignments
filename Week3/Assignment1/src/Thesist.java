import java.util.Random;

public class Thesist extends User{
	int computerNeeded;

	public Thesist(Tutor tutor, int id){
		this(tutor, id, new Random().nextInt(tutor.computerCount));
	}

	public Thesist(Tutor tutor, int id, int computerNeeded) {
		super(tutor, id);
		this.computerNeeded = computerNeeded;
	}

	@Override
	void doTask() {
		System.out.println("Thesist " + id + " got access to the computer with id " + computerNeeded);
		try{
			Thread.sleep((long)(Math.random() * 1000));
		}catch (InterruptedException ie){
			System.out.println("Thread interrupted");
		}
		System.out.println("Thesist " + id + " left the computer with id " + computerNeeded);
	}
}