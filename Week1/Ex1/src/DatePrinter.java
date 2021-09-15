import java.util.Calendar;

public class DatePrinter {
	public static void main(String[] args) {
		String threadName = Thread.currentThread().getName();
		Calendar calendar = Calendar.getInstance();
		while(true){
			System.out.println("The year is: " + calendar.getWeekYear() + "\nThe hour is: " + calendar.get(Calendar.HOUR_OF_DAY) + "\nThe thread's name is: " + threadName);
			try {
				Thread.sleep(2000);
			}catch(InterruptedException e){
				System.err.println("Thread has been interrupted");
			}
		}
	}
}
