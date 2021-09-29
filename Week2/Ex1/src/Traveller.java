public class Traveller implements Runnable{
	int id;

	public Traveller(int id){
		this.id = id;
	}

	@Override
	public void run() {
		System.out.println("Viaggiatore " + id + ": sto acquistando un biglietto");
		try {
			Thread.sleep((long) (Math.random() * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Viaggiatore " + id + ": ho acquistato il biglietto");
	}
}