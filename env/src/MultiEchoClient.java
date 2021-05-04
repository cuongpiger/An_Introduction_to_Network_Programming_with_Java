import javax.annotation.processing.SupportedSourceVersion;
import java.io.*;
import java.net.*;
import java.util.*;

public class MultiEchoClient {
    private static InetAddress host;
    private static final int PORT = 1234;

    private static void sendMessages() {
        Socket socket = null;

        try {
            socket = new Socket(host, PORT);
            Scanner network_input = new Scanner(socket.getInputStream());
            PrintWriter network_output = new PrintWriter(socket.getOutputStream(), true);
            Scanner user_entry = new Scanner(System.in);
            String message, response;

            do {
                System.out.print(">> 'QUIT' to exit: ");
                message = user_entry.nextLine();
                network_output.println(message);
                response = network_input.nextLine();
                System.out.println(">> SERVER: " + response);
            } while (!message.equals("QUIT"));
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            try {
                System.out.println(">> Closing connection...");
                socket.close();
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
            System.out.println("==> Host ID not found!");
            System.exit(1);
        }

        sendMessages();
    }
}
