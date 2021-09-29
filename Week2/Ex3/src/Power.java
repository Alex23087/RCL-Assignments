import java.util.concurrent.Callable;

public class Power implements Callable<Double> {
	double n;
	int pow;

	public Power(double n, int pow){
		this.n = n;
		this.pow = pow;
	}

	@Override
	public Double call(){
		System.out.println("Esecuzione " + n + "^" + pow + " in " + Thread.currentThread().getId());
		return Math.pow(n, pow);
	}
}