import java.io.*;
import java.net.*;
import java.util.*;

public class TCPEchoServer {
    private static ServerSocket server_socket;
    private static final int PORT = 1234;

    private static void handleClient() {
        Socket link = null; // bước 2

        try {
            link = server_socket.accept(); // bước 2
            Scanner input = new Scanner(link.getInputStream()); // bước 3
            PrintWriter output = new PrintWriter(link.getOutputStream(), true); // bước 3

            int num_messages = 0;
            String message = input.nextLine(); // bước 4

            while (!message.equals("***CLOSE***")) {
                System.out.println(">> Message received...");
                output.println(">> Message " + ++num_messages + ". " + message); // bước 4
                message = input.nextLine();
            }

            output.println(">> Number of messages received: " + num_messages); // bước 4
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            try {
                System.out.println(">> Closing connection...");
                link.close(); // bước 5
            } catch (IOException err) {
                System.out.println("==> Unable to disconnect...");
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(">> Opening port " + PORT + "...\n");

        try {
            server_socket = new ServerSocket(PORT); // bước 1
        } catch (IOException err) {
            System.out.println("==> Unable to attach to port!");
            System.exit(1);
        }

        do {
            handleClient();
        } while (true);
    }
}
