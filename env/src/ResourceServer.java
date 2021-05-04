import java.io.*;
import java.net.*;
import java.util.*;

class Resource {
    private int num_resources;
    private final int MAX = 5;

    public Resource(int start_lv) {
        num_resources = start_lv;
    }

    public int getLevel() {
        return num_resources;
    }

    public synchronized int addOne() {
        try {
            while (num_resources >= MAX) {
                wait();
            }

            num_resources += 1;
            notifyAll(); // wake up any waiting customer
        } catch (InterruptedException err) {
            System.out.println("==> Resource.addOne() interrupt: " + err);
        }

        return num_resources;
    }

    public synchronized int takeOne() {
        try {
            while (num_resources == 0) {
                wait();
            }

            num_resources -= 1;
            notify(); // wake up waiting producer
        } catch (InterruptedException err) {
            System.out.println("==> Resource.takeOne() interrupt: " + err);
        }

        return num_resources;
    }
}

class Producer extends Thread {
    private Resource item;

    public Producer(Resource resource) {
        item = resource;
    }

    public void run() {
        int pause;
        int new_level;

        do {
            try {
                new_level = item.addOne();
                System.out.println(">> Producer.new_level: " + new_level);
                pause = (int)(Math.random() * 5000);
                sleep(pause);
            } catch (InterruptedException err) {
                System.out.println("==> Producer.run() interrupt: " + err);
            }
        } while (true);
    }
}

class ClientThread extends Thread {
    private Socket client;
    private Resource item;
    private Scanner input;
    private PrintWriter output;

    public ClientThread(Socket socket, Resource resource) {
        client = socket;
        item = resource;

        try {
            input = new Scanner(client.getInputStream());
            output = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException err) {
            System.out.println("==> ClientThread's constructor exception: " + err.toString());
        }
    }

    public void run() {
        String request = "";

        do {
            request = input.nextLine();

            if (request.equals("1")) {
                item.takeOne();
                output.println("Request granted.");
            }
        } while (!request.equals("0"));

        try {
            System.out.println(">> Closing down connection...");
            client.close();
        } catch (IOException err) {
            System.out.println("==> Unable to disconnect to client!");
        }
    }
}

public class ResourceServer {
    private static ServerSocket server_socket;
    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        try {
            server_socket = new ServerSocket(PORT);
        } catch (IOException err) {
            System.out.println("==> Unable to disconnect!");
            System.exit(1);
        }

        Resource item = new Resource(1);
        Producer producer = new Producer(item);

        producer.start();

        do {
            Socket client = server_socket.accept(); // wait for a client to make connect
            System.out.println(">> Connect to new client " + client.getInetAddress().getCanonicalHostName());
            ClientThread handler = new ClientThread(client, item);
            handler.start();
        } while (true);
    }
}
