import java.net.*;
import java.io.*;
import java.util.Date;

public class DaytimeServer {
    public static void main(String[] args) {
        ServerSocket server;
        final int DAYTIME_PORT = 1234;
        Socket socket;

        try {
            server = new ServerSocket(DAYTIME_PORT);
            System.out.println(">> Server is running on port " + DAYTIME_PORT + "...");

            do {
                socket = server.accept();

                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                Date date = new Date();
                output.println(date);
                socket.close();
            } while (true);
        } catch (IOException err) {
            System.out.println("==> " + err);
        }
    }
}
