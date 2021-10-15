public class Student extends User{
	public int computerInUse = -1;

	public Student(Tutor tutor, int id) {
		super(tutor, id);
	}

	@Override
	void doTask() {
		System.out.println("Student " + id + " got access to computer " + computerInUse);
		try{
			Thread.sleep((long)(Math.random() * 1000));
		}catch (InterruptedException ie){
			System.out.println("Thread interrupted");
		}
		System.out.println("Student " + id + " left computer " + computerInUse);
	}
}