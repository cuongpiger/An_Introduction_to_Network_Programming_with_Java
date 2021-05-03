public class RunnableShowName implements Runnable {
    public void run() {
        int pause;

        for (int i = 0; i < 10; ++i) {
            try {
                pause = (int)(Math.random() * 3000);
                System.out.println(">> " + Thread.currentThread().getName() + " sleeping in " + pause + " miliseconds");
                Thread.sleep(pause);
            } catch (InterruptedException err) {
                System.out.println("==> " + err);
            }
        }
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(new RunnableShowName());
        Thread thread2 = new Thread(new RunnableShowName());

        /*
        // Cách khai báo khác
        RunnableShowName runnable1 = new RunnableShowName();
        RunnableShowName runnable2 = new RunnableShowName();

        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
         */

        thread1.start();
        thread2.start();
    }
}
