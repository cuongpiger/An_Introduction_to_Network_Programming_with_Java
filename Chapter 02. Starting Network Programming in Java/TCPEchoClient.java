import java.net.*;
import java.io.*;
import java.util.*;

public class TCPEchoClient {
    private static InetAddress host;
    private static final int PORT = 1234;

    private static void accessServer() {
        Socket link = null; // bước 1

        try {
            link = new Socket(host, PORT); // bước 1
            Scanner input = new Scanner(link.getInputStream()); // bước 2
            PrintWriter output = new PrintWriter(link.getOutputStream(), true); // bước 2
            Scanner user_entry = new Scanner(System.in);

            String message, response;

            do {
                System.out.print(">> Enter your message: ");
                message = user_entry.nextLine();
                output.println(message); // bước 3
                response = input.nextLine(); // bước 3
                System.out.println(">> SERVER: " + response);
            } while (!message.equals("***CLOSE***"));
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            try {
                System.out.println(">> Closing connection...");
                link.close();
            } catch (IOException err) {
                System.out.println("==> Unable to disconnect!");
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) {
        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException err) {
            System.out.println("==> Host ID not found...");
            System.exit(1);
        }

        accessServer();
    }
}
