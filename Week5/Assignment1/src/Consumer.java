import java.io.File;
import java.util.List;

public class Consumer implements Runnable {
	private final List<String> queue;

	public Consumer(List<String> queue){
		this.queue = queue;
	}

	@Override
	public void run() {
		while(true){
			String element = "";
			synchronized (queue) {
				try {
					while (queue.isEmpty()) {
						queue.wait();
					}
					element = queue.get(0);
					if(element.equals("terminate")){
						break;
					}else {
						queue.remove(0);
					}
				} catch (InterruptedException ignored) {}
			}
			printDirectoryContents(element);
		}
		System.out.println("Thread " + Thread.currentThread().getId() + " terminating");
	}

	private void printDirectoryContents(String path){
		File dir = new File(path);
		String[] contents = dir.list();
		if (contents == null) {
			return;
		}
		for(String p : contents){
			System.out.println("Thread " + Thread.currentThread().getId() + ": " + path + File.separator + p);
		}
	}
}