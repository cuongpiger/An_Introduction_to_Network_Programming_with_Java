public class RunnableHelloCount implements Runnable {
    private Thread thread1, thread2;

    public RunnableHelloCount() {
        thread1 = new Thread(this);
        thread2 = new Thread(this);

        thread1.start();
        thread2.start();
    }

    public void run() {
        int pause;

        for (int i = 0; i < 10; ++i) {
            try {
                pause = (int) (Math.random() * 3_000);
                System.out.println(">> " + Thread.currentThread().getName() + " sleeping in " + pause + " miliseconds");
                Thread.sleep(pause);
            } catch (InterruptedException err) {
                System.out.println("==> " + err);
            }
        }
    }

    public static void main(String[] args) {
        RunnableHelloCount hello = new RunnableHelloCount();
    }
}
