import java.io.File;
import java.util.List;

public class Producer implements Runnable {
	private final String path;
	private final List<String> queue;

	public Producer(String path, List<String> queue){
		this.path = path;
		this.queue = queue;
	}

	@Override
	public void run() {
		addFilesToListRecursively(path);
		synchronized (queue){
			queue.add("terminate");
			queue.notifyAll();
		}
	}

	private void addFilesToListRecursively(String path){ //Only adds files to the queue if they're directories
		File currentDirectory = new File(path);
		if(currentDirectory.isDirectory()){
			synchronized (queue) {
				queue.add(path);
				queue.notify();
			}
			String[] files = currentDirectory.list();

			if(files == null){
				return;
			}
			for(String p : files){
				addFilesToListRecursively(path + File.separator + p);
			}
		}
	}
}