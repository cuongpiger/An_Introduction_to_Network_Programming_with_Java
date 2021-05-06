import java.io.*;
import java.net.*;
import java.util.*;

class Personnel implements Serializable {
    private long payroll_num;
    private String surname;
    private String firstname;

    public Personnel(long p, String s, String f) {
        payroll_num = p;
        surname = s;
        firstname = f;
    }

    public long get_payroll_num() {
        return payroll_num;
    }

    public String get_surname() {
        return surname;
    }

    public String get_firstname() {
        return firstname;
    }

    public void set_surname(String s) {
        surname = s;
    }
}

public class PersonnelServer {
    private static ServerSocket server_socket;
    private static final int PORT = 1234;
    private static Socket socket;
    private static ArrayList<Personnel> staff_list_out;
    private static Scanner in_stream;
    private static ObjectOutputStream out_stream;

    private static void startServer() {
        do {
            try {
                socket = server_socket.accept();
                in_stream = new Scanner(socket.getInputStream());
                out_stream = new ObjectOutputStream(socket.getOutputStream());

                String message = in_stream.nextLine();
                if (message.equals("SEND PERSONNEL DETAILS")) {
                    out_stream.writeObject(staff_list_out);
                    out_stream.close();
                }

                System.out.println(">> Closing connection...");
                socket.close();
            } catch (IOException err) {
                err.printStackTrace();
            }
        } while (true);
    }

    public static void main(String[] args) {
        System.out.println(">> Opening on port " + PORT);

        try {
            server_socket = new ServerSocket(PORT);
        } catch (IOException err) {
            System.out.println("==> Unable to attach to port");
            System.exit(1);
        }

        staff_list_out = new ArrayList<>();
        Personnel[] staff = {
                new Personnel(123456, "Duong", "Cuong"),
                new Personnel(234567, "Nguyen", "Que"),
                new Personnel(345678, "Tran", "Phuong")
        };

        for (var person : staff) staff_list_out.add(person);
        startServer();
    }
}
