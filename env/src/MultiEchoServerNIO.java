import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class MultiEchoServerNIO {
    private static ServerSocketChannel server_socket_channel;
    private static final int PORT = 1234;
    private static Selector selector; // (1)

    private static void processConnections() {

    }

    public static void main(String[] args) {
        ServerSocket server_socket;
        System.out.println(">> Opening on port " + PORT);

        try {
            server_socket_channel = ServerSocketChannel.open();
            server_socket_channel.configureBlocking(false); // ko chặn
            server_socket = server_socket_channel.socket();
            InetSocketAddress net_address = new InetSocketAddress(PORT); // (2)
            server_socket.bind(net_address); // liên kết socket này đến port

            selector = Selector.open(); // (3)
            server_socket_channel.register(selector, SelectionKey.OP_ACCEPT); // (4)
        } catch (IOException err) {
            System.out.println("==> main() exception: " + err.toString());
            System.exit(1);
        }

        processConnections();
    }
}


/*
(1): Giám sát các kết nối mới và việc truyền dữ liệu của các kết nối hiện có, mỗi channel
    (dù là SocketChannel hay ServerSocketChannel) phải đăng kí vs object này loại hành
    động mà channel này quan tâm thông qua p.thức `register`. Có 4 SelectionKey dùng để
    xác định loại hành động mà channel quan tâm là:
        + SelectionKey.OP_ACCEPT: theo dõi kết nối mới.
        + SelectionKey.OP_CONNECT
        + SelectionKey.OP_READ: truyền dữ liệu từ kết nối hiện có.
        + SelectionKey.OP_WRITE

(2): Tạo ra port mà server sẽ chạy.

(3): Tạo ra object seletor để phát hiện dữ liệu truyền vào từ các channel.

(4): ServerSocketChannel đăng kí vs selector để nhận các connection mới.
 */