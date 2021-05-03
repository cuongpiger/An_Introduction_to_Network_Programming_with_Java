import java.io.*;
import java.net.*;
import java.util.*;

public class EmailClient {
    private static InetAddress host;
    private static final int PORT = 1234;
    private static String name;
    private static Scanner network_input, user_entry;
    private static PrintWriter network_output;

    private static void talkToServer() throws IOException {
        Socket link = null;

        try {
            link = new Socket(host, PORT);
            network_input = new Scanner(link.getInputStream());
            network_output = new PrintWriter(link.getOutputStream(), true);
            user_entry = new Scanner(System.in);

            String option;

            do {
                System.out.print("\n>> Would you like to 'send' or 'read' ('close' to disconnect): ");
                option = user_entry.nextLine().trim().toLowerCase();

                if (option.equals("send")) {
                    doSend();
                } else if (option.equals("read")) {
                    doRead();
                } else if (option.equals("close")) { // send disconnect signal
                    network_output.println(name);
                    network_output.println("send");
                    network_output.println("close");
                }
            } while (!option.equals("close"));
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            try {
                System.out.println(">> Closing connection...");
                link.close();
            } catch (IOException err) {
                System.out.println("==> Unable to disconnect");
                System.exit(1);
            }
        }
    }

    private static void doSend() {
        System.out.print(">> Enter one-line message: ");
        String message = user_entry.nextLine();
        network_output.println(name);
        network_output.println("send");
        network_output.println(message);
    }

    private static void doRead() throws IOException {
        String response = network_input.nextLine();
        System.out.println(">> SERVER: " + response);
    }

    public static void main(String[] args) throws IOException {
        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException err) {
            System.out.println("==> Host ID not found");
            System.exit(1);
        }

        user_entry = new Scanner(System.in);

        do {
            System.out.print("Enter name('Naruto' or 'Sasuke'): ");
            name = user_entry.nextLine();
        } while (!name.equals("Naruto") && !name.equals("Sasuke"));

        talkToServer();
    }
}
