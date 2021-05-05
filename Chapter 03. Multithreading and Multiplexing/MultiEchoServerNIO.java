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
        do {
            try {
                int num_keys = selector.select(); // (5)

                if (num_keys > 0) {
                    Set even_keys = selector.selectedKeys(); // (6)
                    Iterator key_cycler = even_keys.iterator(); // (7)

                    while (key_cycler.hasNext()) {
                        SelectionKey key = (SelectionKey) key_cycler.next();
                        int key_ops = key.readyOps();

                        if ((key_ops & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) { // new connection
                            acceptConnection(key);
                            continue;
                        }

                        if ((key_ops & SelectionKey.OP_READ) == SelectionKey.OP_READ) { // client này đã tồn tại
                            acceptData(key);
                        }
                    }
                }
            } catch (IOException err) {
                System.out.println("==> MultiEchoServerNIO.processConnections() exception: " + err.toString());
                System.exit(1);
            }
        } while (true);
    }

    private static void acceptConnection(SelectionKey key) throws IOException {
        SocketChannel socket_channel;
        Socket socket;

        socket_channel = server_socket_channel.accept();
        socket_channel.configureBlocking(false);
        socket = socket_channel.socket();
        System.out.println(">> Connection on " + socket + "...");

        socket_channel.register(selector, SelectionKey.OP_READ);
        selector.selectedKeys().remove(key); // loại bỏ key này vì đã dc xử lí rồi
    }

    private static void acceptData(SelectionKey key) throws IOException {
        SocketChannel socket_channel;
        Socket socket;

        ByteBuffer buffer = ByteBuffer.allocate(2048); // (8)
        socket_channel = (SocketChannel) key.channel();
        buffer.clear();

        int num_bytes = socket_channel.read(buffer);
        System.out.println(">> Reading " + num_bytes + " bytes...");
        socket = socket_channel.socket();

        if (num_bytes == -1) {
            key.cancel();
            System.out.println(">> Closing socket " + socket + "...");
            closeSocket(socket);
        } else {
            try {
                buffer.flip(); // đặt con trỏ lại vào đầu bộ đệm
                while (buffer.remaining() > 0) {
                    socket_channel.write(buffer); // send data
                }
            } catch (IOException err) {
                System.out.println(">> Closing socket " + socket + "...");
                closeSocket(socket);
            }
        }

        selector.selectedKeys().remove(key);
    }

    private static void closeSocket(Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException err) {
            System.out.println("==> Unable to disconnect!");
        }
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

(5): Lấy số lượng hành động (kết nối mới hoặc chuyền dữ liệu từ các connection có sẵn).

(6): Lấy các SelectionKeys cho vào object Set.

(7): Đối tượng Iterator dùng để loop qua Set.

(8): Dùng để ghi/đọc dữ liệu từ SocketChannel.
 */