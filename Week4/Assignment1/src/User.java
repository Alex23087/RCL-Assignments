import java.util.Random;

public abstract class User implements Runnable{
	protected Tutor tutor;
	protected int id;

	public User(Tutor tutor, int id){
		this.tutor = tutor;
		this.id = id;
	}

	@Override
	public void run() {
		int k = new Random().nextInt(10); //Number of times the user will repeat the task
		while(k --> 0){
			try {
				Thread.sleep((long)(Math.random() * 1000));
			}catch(InterruptedException ie){
				System.out.println("Thread interrupted");
			}
			tutor.enqueue(this);
		}
	}

	abstract void doTask();
}