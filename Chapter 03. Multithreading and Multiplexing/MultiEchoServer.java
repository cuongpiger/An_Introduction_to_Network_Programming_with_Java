import java.io.*;
import java.net.*;
import java.util.Scanner;

class ClientHandler extends Thread {
    private Socket client;
    private Scanner input;
    private PrintWriter output;

    public ClientHandler(Socket socket) {
        client = socket;

        try {
            input = new Scanner(client.getInputStream());
            output = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    public void run() {
        String received;

        do {
            received = input.nextLine();
            output.println("ECHO: " + received);

            System.out.println(">> Sending message to " + client.getLocalAddress());
        } while (!received.equals("QUIT"));

        try {
            if (client != null) {
                System.out.println(">> Closing connection...");
                client.close();
            }
        } catch (IOException err) {
            System.out.println("==> Unable to disconnect!");
        }
    }
}

public class MultiEchoServer {
    private static ServerSocket server_socket;
    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        try {
            server_socket = new ServerSocket(PORT);
            System.out.println(">> Opening on PORT " + PORT);
        } catch (IOException err) {
            System.out.println("==> Unable to set up port.");
            System.exit(1);
        }

        do {
            Socket client = server_socket.accept();
            System.out.println(">> New client accepted.");

            ClientHandler handler = new ClientHandler(client);
            handler.start();
        } while (true);
    }
}
