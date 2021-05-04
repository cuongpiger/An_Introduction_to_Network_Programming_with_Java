import java.io.*;
import java.net.*;
import java.util.*;

public class CusumerClient {
    private static InetAddress host;
    private static final int PORT = 1234;

    private static void sendMessages() {
        Socket socket = null;

        try {
            socket = new Socket(host, PORT);
            Scanner network_input = new Scanner(socket.getInputStream());
            PrintWriter network_output = new PrintWriter(socket.getOutputStream(), true);
            Scanner user_entry = new Scanner(System.in);
            String signal, response;

            while (true) {
                System.out.print(">> Enter 1 for resource or 0 to quit: ");
                signal = user_entry.nextLine();

                network_output.println(signal); // send signal to server
                if (signal.equals("1")) {
                    response = network_input.nextLine();
                    System.out.println(">> SERVER: " + response);
                } else {
                    break;
                }
            }
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