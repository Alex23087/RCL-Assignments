public class Student extends User{
	public Student(Tutor tutor, int id) {
		super(tutor, id);
	}

	@Override
	void doTask() {
		System.out.println("Student " + id + " got access to a computer");
		try{
			Thread.sleep((long)(Math.random() * 1000));
		}catch (InterruptedException ie){
			System.out.println("Thread interrupted");
		}
		System.out.println("Student " + id + " left the computer");
	}
}