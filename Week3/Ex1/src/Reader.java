public class Reader implements Runnable{
	Counter counter;

	public Reader(Counter counter){
		this.counter = counter;
	}

	@Override
	public void run() {
		System.out.println(counter.get());
	}
}