class HelloThread extends Thread {
    public void run() {
        int pause;

        for (int i = 0; i < 5; ++i) {
            try {
                System.out.println(">> Hello!");
                pause = (int)(Math.random() * 3000);
                sleep(pause);
            } catch (InterruptedException err) {
                System.out.println("==> " + err);
            }
        }
    }
}

class CountThread extends Thread {
    public void run() {
        int pause;

        for (int i = 0; i < 5; ++i) {
            try {
                System.out.println(">> " + i);
                pause = (int)(Math.random() * 3_000);
                sleep(pause);
            } catch (InterruptedException err) {
                System.out.println("==> " + err);
            }
        }
    }
}

public class ThreadHelloCount {
    public static void main(String[] args) {
        HelloThread hello = new HelloThread();
        CountThread count = new CountThread();

        hello.start();
        count.start();
    }
}
