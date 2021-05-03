import java.io.*;
import java.net.*;
import java.util.*;

class InvalidClientException extends Exception {
    public InvalidClientException() {
        super("==> Invalid client name!");
    }

    public InvalidClientException(String message) {
        super(message);
    }
}

class InvalidRequestException extends Exception {
    public InvalidRequestException() {
        super("==> Invalid request!");
    }

    public InvalidRequestException(String message) {
        super(message);
    }
}

public class EmailServer {
    private static ServerSocket server_socket;
    private static final int PORT = 1234;
    private static final String client1 = "Naruto";
    private static final String client2 = "Sasuke";
    private static final int MAX_MESSAGES = 10;
    private static String[] mailbox1 = new String[MAX_MESSAGES];
    private static String[] mailbox2 = new String[MAX_MESSAGES];
    private static int message_inbox1 = 0;
    private static int message_inbox2 = 0;


    private static void runService() throws InvalidClientException, InvalidRequestException {
        try {
            Socket link = server_socket.accept();
            Scanner input = new Scanner(link.getInputStream());
            PrintWriter output = new PrintWriter(link.getOutputStream(), true);
            String name = input.nextLine();
            String send_read = input.nextLine();

            if (!name.equals(client1) && !name.equals(client2)) throw new InvalidClientException();

            if (!send_read.equals("send") && !send_read.equals("read")) throw new InvalidRequestException();

            System.out.println("\n>> " + name + " " + send_read + "ing mail...");

            if (name.equals(client1)) {
                if (send_read.equals("send")) {
                    doSend(mailbox2, message_inbox2, input);
                    message_inbox2 += message_inbox2 < MAX_MESSAGES ? 1 : 0;
                } else {
                    doRead(mailbox1, message_inbox1, output);
                    message_inbox1 = 0;
                }
            } else { // from `client2`
                if (send_read.equals("send")) {
                    doSend(mailbox1, message_inbox1, input);
                    message_inbox1 += message_inbox1 < MAX_MESSAGES ? 1 : 0;
                } else {
                    doRead(mailbox2, message_inbox2, output);
                    message_inbox2 = 0;
                }

                link.close();
            }
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    private static void doSend(String[] mailbox, int message_inbox, Scanner input) {
        String message = input.nextLine();

        if (message_inbox == MAX_MESSAGES) {
            System.out.println("<< Message box full!");
        } else {
            mailbox[message_inbox] = message;
        }
    }

    private static void doRead(String[] mailbox, int message_inbox, PrintWriter output) {
        System.out.println(">>\nSending " + message_inbox + " message(s).\n");
        output.println(message_inbox);
        for (int i = 0; i < message_inbox; ++i) {
            output.println(mailbox[i]);
        }
    }

    public static void main(String[] args) {
        System.out.println(">> Opening connection on port " + PORT);

        try {
            server_socket = new ServerSocket(PORT);
        } catch (IOException err) {
            System.out.println("==> Unable to attach to port!");
            System.exit(1);
        }

        do {
            try {
                runService();
            } catch (InvalidClientException err) {
                System.out.println("==> ERROR: " + err);
            } catch (InvalidRequestException err) {
                System.out.println("==> ERROR: err");
            }
        } while (true);
    }
}
