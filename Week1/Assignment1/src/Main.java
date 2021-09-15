public class Main {
	public static void main(String[] args) {
		double accuracy;
		try {
			accuracy = Double.parseDouble(args[0]);
		}catch(NumberFormatException nfe){
			System.err.println("The value passed as accuracy is in an invalid format");
			return;
		}

		long interruptionTime;
		try {
			interruptionTime = Long.parseLong(args[1]);
		}catch(NumberFormatException nfe) {
			System.err.println("The value passed as time is in an invalid format");
			return;
		}

		PiRunnable piRunnable = new PiRunnable(accuracy);
		Thread piThread = new Thread(piRunnable);
		piThread.start();

		try {
			Thread.sleep(interruptionTime);
			piThread.interrupt();
			piThread.join();
		}catch(InterruptedException ie){
			System.err.println("The main thread has been interrupted");
		}
	}
}
