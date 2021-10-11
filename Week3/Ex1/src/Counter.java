import java.util.concurrent.locks.ReentrantLock;

/**
 * Counter modella il contatore
 * @author Samuel Fabrizi
 * @version 1.0
 */

public class Counter {
	/**
	 * rappresenta il contatore
	 */
	private int contatore = 0;
	private ReentrantLock lock = new ReentrantLock();

	/**
	 * incrementa di 1 unità il valore del contatore
	 */
	public void increment(){
		try{
			lock.lock();
			contatore++;
		}finally{
			lock.unlock();
		}
	}

	/**
	 *
	 * @link Counter#contatore
	 */
	public int get(){
		int out;
		try{
			lock.lock();
			out = contatore;
		}finally {
			lock.unlock();
		}
		return out;
	}
}