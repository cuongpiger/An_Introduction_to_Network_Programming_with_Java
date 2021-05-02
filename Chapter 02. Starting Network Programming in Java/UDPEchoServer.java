import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class UDPEchoServer {
    private static final int PORT = 1234;
    private static DatagramSocket datagram_socket;
    private static DatagramPacket in_packet, out_packet;
    private static byte[] buffer;

    public static void handleClient() {
        try {
            String message_in, message_out;
            int num_messages = 0;
            InetAddress client_address = null;
            int client_port;

            do {
                buffer = new byte[256]; // bước 2
                in_packet = new DatagramPacket(buffer, buffer.length); // bước 3
                datagram_socket.receive(in_packet); // bước 4
                client_address = in_packet.getAddress(); // bước 5
                client_port = in_packet.getPort(); // bước 6

                message_in = new String(in_packet.getData(), 0, in_packet.getLength()); // bước 6
                System.out.println(">> Message received...");
                message_out = ">> Message " + ++num_messages + ". " + message_in;

                out_packet = new DatagramPacket(message_out.getBytes(), message_out.length(), client_address, client_port); // bước 7
                datagram_socket.send(out_packet); // bước 8
            } while (true);
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            System.out.println(">> Closing connection...");
            datagram_socket.close(); // bước 9
        }
    }

    public static void main(String[] args) {
        System.out.println(">> Opening port " + PORT + "...");

        try {
            datagram_socket = new DatagramSocket(PORT);
        } catch (SocketException err) {
            System.out.println("==> Unable to open port " + PORT); // bước 1
            System.exit(1);
        }

        handleClient();
    }
}
