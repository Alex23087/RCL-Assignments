public class PersonTask implements Runnable{
	int id;

	public PersonTask(int id){
		this.id = id;
	}

	@Override
	public void run() {
		System.out.println("Person " + id + " being served");
		try {
			Thread.sleep((long) (Math.random() * 1000d));
		} catch (InterruptedException e) {
			System.out.println("Person " + id + " has been interrupted");
		}
		System.out.println("Person " + id + " finished and left the office");
	}
}