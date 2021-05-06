import java.io.*;
import java.net.*;
import java.util.*;

public class PersonnelClient {
    private static InetAddress host;
    private static final int PORT = 1234;

    private static void talkToServer() throws ClassNotFoundException {
        try {
            Socket socket = new Socket(host, PORT);
            ObjectInputStream in_stream = new ObjectInputStream(socket.getInputStream());
            PrintWriter out_stream = new PrintWriter(socket.getOutputStream(), true);
            Scanner user_entry = new Scanner(System.in);
            out_stream.println("SEND PERSONNEL DETAILS");
            ArrayList<Personnel> response = (ArrayList<Personnel>) in_stream.readObject();
            System.out.println(">> Closing connection.");
            socket.close();

            int staff_count = 0;
            for (var person : response) {
                System.out.println(">> Staff number: " + ++staff_count);
                System.out.println("   >> Payroll number: " + person.get_payroll_num());
                System.out.println("   >> Surname: " + person.get_surname());
                System.out.println("   >> Firstname: " + person.get_firstname());
            }

            System.out.println("\n");
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException err) {
            System.out.println("==> Host ID not found!");
            System.exit(1);
        }

        talkToServer();
    }
}
