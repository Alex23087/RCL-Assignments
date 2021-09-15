public class PiRunnable implements Runnable{
	private double accuracy;

	public PiRunnable(double accuracy){
		super();
		this.accuracy = accuracy;
	}

	@Override
	public void run() {
		double currentPi = 4d/1d;
		double currentFactor = 1d;
		short currentMultiplier = 1;
		Thread currentThread = Thread.currentThread();

		while(Math.abs(currentPi - Math.PI) >= accuracy && !currentThread.isInterrupted()){
			currentFactor += 2;
			currentMultiplier *= -1;
			currentPi = currentPi + (currentMultiplier * (4d/currentFactor));
		}
		System.out.println(currentPi);

		//Calling System.exit to terminate the main thread,
		// otherwise it would wait even if the thread has finished calculating pi
		// up to the specified accuracy. Another way to handle this would be
		// with an interrupt to the main thread.
		System.exit(0);
	}
}
