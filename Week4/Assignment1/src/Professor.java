public class Professor extends User{
	public Professor(Tutor tutor, int id) {
		super(tutor, id);
	}

	@Override
	void doTask() {
		System.out.println("Professor " + id + " got access to the lab");
		try{
			Thread.sleep((long)(Math.random() * 1000));
		}catch (InterruptedException ie){
			System.out.println("Thread interrupted");
		}
		System.out.println("Professor " + id + " left the lab");
	}
}