import java.io.*;
import java.net.*;
import java.util.*;

public class UDPEchoClient {
    private static InetAddress host;
    private static final int PORT = 1234;
    private static DatagramSocket datagram_socket;
    private static DatagramPacket in_packet, out_packet;
    private static byte[] buffer;

    private static void accessServer() {
        try {
            datagram_socket = new DatagramSocket(); // bước 1
            Scanner user_entry = new Scanner(System.in);
            String message = "", response = "";

            do {
                System.out.print("Enter message: ");
                message = user_entry.nextLine();

                if (!message.equals("***CLOSE***")) {
                    out_packet = new DatagramPacket(message.getBytes(), message.length(), host, PORT); // bước 2
                    datagram_socket.send(out_packet); // bước 3
                    buffer = new byte[256]; // bước 4
                    in_packet = new DatagramPacket(buffer, buffer.length); // bước 5
                    datagram_socket.receive(in_packet); // bước 6
                    response = new String(in_packet.getData(), 0, in_packet.getLength()); // bước 7

                    System.out.println(">> SERVER: " + response);
                }
            } while (!message.equals("***CLOSE***"));
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            System.out.println(">> Closing connection...");
            datagram_socket.close(); // bước 8
        }
    }

    public static void main(String[] args) {
        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException err) {
            System.out.println("==> Host ID not found!");
            System.exit(1);
        }

        accessServer();
    }
}
