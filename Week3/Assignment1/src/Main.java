import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Tutor tutor = new Tutor();
        int userCount = new Random().nextInt(30);
        for(int i = 0; i < userCount; i++) {
            User newUser;
            if (Math.random() < 0.1) {
                newUser = new Professor(tutor, i);
            } else if (Math.random() < 0.3) {
                newUser = new Thesist(tutor, i);
            } else {
                newUser = new Student(tutor, i);
            }
            new Thread(newUser).start();
        }
    }
}