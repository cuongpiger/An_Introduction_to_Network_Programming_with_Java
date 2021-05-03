public class ThreadShowName extends Thread {
    public void run() {
        int pause;

        for (int i = 0; i < 10; ++i) {
            try {
                pause = (int) (Math.random() * 3000);
                System.out.println(">> " + getName() + " sleeping in " + pause + " milisecond");
                sleep(pause); // sleep from 0 to 3 second
            } catch (InterruptedException err) {
                System.out.println("==> " + err);
            }
        }
    }

    public static void main(String[] args) {
        ThreadShowName thread1, thread2;
        thread1 = new ThreadShowName();
        thread2 = new ThreadShowName();

        thread1.start();
        thread2.start();
    }
}
