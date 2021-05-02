# 1. The `InetAddress` Class
* Nhập vào **hostname** sẽ trả về **địa chỉ IP** của hostname đó.
###### IPFinder.java _[source code](IPFinder.java)_
```java
import java.net.*;
import java.util.*;

public class IPFinder {
    public static void main(String[] args) {
        String host;
        Scanner input = new Scanner(System.in);
        InetAddress address;

        System.out.print("\uD83D\uDCBB Enter the host name: ");
        host = input.next();

        try {
            address = InetAddress.getByName(host);
            System.out.println("\uD83D\uDCFA IP Address: " + address.toString());
        } catch (UnknownHostException err) {
            System.out.println("\uD83D\uDEAB Could not find " + host);
        }
    }
}
```
![](../images/02_00.png)

<hr>

* Chương trình lấy ra thông tin **hostname** và **địa chỉ IP** của máy hiện tại.
###### MyLocalIPAddress.java _[source code](MyLocalIPAddress.java)_
```java
import java.net.*;

public class MyLocalIPAddress {
    public static void main(String[] args) {
        try {
            InetAddress address = InetAddress.getLocalHost();
            System.out.println("\uD83D\uDCFA " + address);
        } catch (UnknownHostException err) {
            System.out.println("\uD83D\uDEAB Could not find your local address!");
        }
    }
}
```
![](../images/02_01.png)

# 2. Using Sockets
## 2.1. TCP Sockets
* Một liên kết giao tiếp dc tạo bằng TCP/IP là một liên kết **hướng kết nối**. Điều này có nghĩa là kết nối giữa server và client vẫn sẽ mở trong suốt quá trình diễn ra cuộc "đối thoại" giữa server và client và chỉ đóng khi server hoặc client thực hiện "chấm dứt kết nối". 
* Trước tiên chúng ta sẽ tiếp cận bên phía server trước, để thiết lập socket cho server cần 5 bước.
  * **Bước 1**: Tạo ra đối tượng `ServerSocket`.
    * Constructor `ServerSocket` cần truyền vào một tham số gọi là **port**. Ví dụ:
      ```java
      ServerSocket serversocket = new ServerSocket(1234); // port là 1234
      ```
    * Ở ví dụ trên, server sẽ chờ một kết nối nào đó từ client trên port 1234.  
  * **Bước 2**: Đặt server vào trạng thái "chờ".
    * Server sẽ tiến hành chờ một kết nối nào đó từ client đến nó. Server thực hiện dc điều này bằng cách gọi p.thức `ServerSocket.accept`, p.thức này trả về một object socket khi server đã chấp nhận kết nối từ phía client. Ví dụ:
      ```java
      Socket link = serversocket.accept();
      ``` 
  * **Bước 3**: Thiết lập **input** và **output stream**.
    * P.thức `getInputStream` và `getOutputStream` của class `Socket` dc s.dụng để tham chiếu đến các **stream** mà socket trả về ở _**bước 2**_. Các stream này dc dùng để g.tiếp vs client vừa kết nối. Ví dụ dưới đây là dùng để lấy nội dung mà client gửi qua cho server qua stream:
    ```java
    Scanner input = new Scanner(link.getInputStream());
    ``` 
    * Bây giờ, chúng ta cần tạo một **chiếc hộp** chứa tất cả mọi thứ mà server muốn gửi cho client, chiếc hộp này dc tạo thông qua object `PrintWriter`, đối số  thứ hai mang giá trị `true` sẽ tiến hành xóa bộ đêm _(chung cũng méo biết nó là gì)_.
    ```java
    PrintWriter output = new PrintWriter(link.getOutputStream(), true);
    ```
  * **Bước 4**: Gửi và nhận dữ liệu.
    * Sử dụng p.thức `nextLine` để nhận dữ liệu và `println` để gửi dữ liệu. Ví dụ:
    ```java
    output.println("Gửi dữ liệu...");
    String input = input.nextLine(); // nhận dữ liệu
    ```
  * **Bước 5**: Đóng kết nối _(sau khi đã hoàn thành cuộc **"đối thoại"**)_.
    ```java
    link.close();
    ```
* Dưới đây là một ví dụ minh họa cho 5 bước trên:
  * Một server tiến hành nhận các tin nhắn từ client, các tin nhắn mà client gửi đến sẽ dc server đánh số và gửi lại client tin nhắn này kèm theo số dc đánh cho tin nhắn đó. Server và client sẽ luân phiên gửi qua gửi lại như vậy cho đến khi client gửi tin nhắn '***CLOSE***' khi muốn dừng kết nối. Khi máy chủ nhận dc tin nhắn muốn đóng kết nối này, server sẽ tiến hành đóng kết nối đến client này.
* Dưới đây là code demo cho toàn bộ những lí thuyết trên, cụ thể tiến hành mở một port 1234 và chờ một kết nối từ client nào đó:
###### TCPEchoServer.java _[source code](TCPEchoServer.java)_
```java
import java.io.*;
import java.net.*;
import java.util.*;

public class TCPEchoServer {
    private static ServerSocket server_socket;
    private static final int PORT = 1234;

    private static void handleClient() {
        Socket link = null; // bước 2

        try {
            link = server_socket.accept(); // bước 2
            Scanner input = new Scanner(link.getInputStream()); // bước 3
            PrintWriter output = new PrintWriter(link.getOutputStream(), true); // bước 3

            int num_messages = 0;
            String message; // bước 4

            while (true) {
                message = input.nextLine();

                if (message.equals("***CLOSE***")) {
                    output.println(">> Number of messages received: " + num_messages); // bước 4

                    break;
                }

                System.out.println(">> Message received...");
                output.println(++num_messages + ". " + message); // bước 4
            }

        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            try {
                System.out.println(">> Closing connection...");
                link.close(); // bước 5
            } catch (IOException err) {
                System.out.println("==> Unable to disconnect...");
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(">> Opening port " + PORT + "...\n");

        try {
            server_socket = new ServerSocket(PORT); // bước 1
        } catch (IOException err) {
            System.out.println("==> Unable to attach to port!");
            System.exit(1);
        }

        do {
            handleClient();
        } while (true);
    }
}
```
![](../images/02_02.png)

<hr>

* Bây giờ chúng ta sẽ tiến hành thiết lập cho client, bao gồm 4 bước:
  * **Bước 1**: Thiếp lập kết nối đến máy chủ.
    * Chúng ta cần tạo ra một socket object, bao gồm **hai đối số**:
      * Địa chỉ IP của server.
      * Port mà server cung cấp dịch vụ mà client cần.
    * Để đơn giản, chúng ta sẽ đặt server và client trên cùng một máy chủ, điều này sẽ cho phép truy xuất dễ dàng đến địa chỉ IP thông qua p.thức `InetAddress.getLocalHost`. Ví dụ:
      ```java
      Socket link = new Socket(InetAddress.getLocalHost(), 1234);
      ```
  * **Bước 2**: Thiếp lập input và output stream.
    * Làm tương tự như cách ta làm cho server.
  * **Bước 3**: Gửi và nhận dữ liệu.
    * Tương tư cách làm ở server.
  * **Bước 4**: Đóng kết nối.
    * Tương tự như server luôn 😅.
* Dưới đây là code demo, lưu ý ta cần phải khởi chạy server trc sau đó mới chạy client, nếu ko sẽ xảy ra lỗi.

###### TCPEchoClient.java _[source code](TCPEchoClient.java)_
```java
import java.net.*;
import java.io.*;
import java.util.*;

public class TCPEchoClient {
    private static InetAddress host;
    private static final int PORT = 1234;

    private static void accessServer() {
        Socket link = null; // bước 1

        try {
            link = new Socket(host, PORT); // bước 1
            Scanner input = new Scanner(link.getInputStream()); // bước 2
            PrintWriter output = new PrintWriter(link.getOutputStream(), true); // bước 2
            Scanner user_entry = new Scanner(System.in);

            String message, response;

            do {
                System.out.print(">> Enter your message: ");
                message = user_entry.nextLine();
                output.println(message); // bước 3
                response = input.nextLine(); // bước 3
                System.out.println(">> SERVER: " + response);
            } while (!message.equals("***CLOSE***"));
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            try {
                System.out.println(">> Closing connection...");
                link.close();
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
            System.out.println("==> Host ID not found...");
            System.exit(1);
        }

        accessServer();
    }
}
```
![](../images/02_03.png)

## 2.2. Datagram (UDP) Sockets
* Không giống như TCP/IP, UDP là **ko kết nối**. Điều này có nghĩa là kết nối giữa server và client ko dc duy trì trog suốt thời gian của cuộc hội thoại. Thay vào đó các gói của UDP sẽ dc gửi đi chỉ khi cần thiết.
* Vì kết nối ko dc duy trì giữa các phiên, cho nên server ko cần phải tạo một object socket riêng biệt cho từng client như đã làm cho TCP/IP. Thay vì tạo một object `ServerSocket`, server sẽ tạo một object `DatagramSocket`.
* Trc tiên, ta sẽ thiết lập ở phía server trc, bao gồm 9 bước:
  * **Bước 1**: Tạo ra object `DatagramSocket` với đối số là port.
    ```java
    DatagramSocket datagram_socket = new DatagramSocket(1234);
    ``` 
  * **Bước 2**: Tạo bộ đệm cho các gói tin.
    ```java
    byte[] buffer = new byte[256];
    ```
  * **Bước 3**: Tạo object `DatagramPacket` cho các gói dữ liệu đến, gồm hai đối số là mảng `buffer` và kích thước của mảng `buffer` này.
    ```java
    DatagramPacket in_packet = new DatagramPacket(buffer, buffer.length);
    ```
  * **Bước 4**: Chấp nhận các gói tin đến.
    ```java
    datagram_socket.receive(in_packet);
    ```
  * **Bước 5**: Chấp nhận IP và port của client gửi gói tin.
    ```java
    InetAddress client_address = in_packet.getAddress();
    int client_port = in_packet.getPort();
    ```   
  * **Bước 6**: Nhận dữ liệu từ `in_packet`.
    * Để thuận tiện cho việc xử lí, dữ liệu sẽ dc casting sang `String` bằng cách sử dụng overloading-constructor của `String` gồm 3 đối số:
      * `in_packet`.
      * vị trí bắt đầu cast trong `in_packet`, ở đây luôn là 0.
      * số byte cần lấy, luôn là `in_packet.getLength()`.    
      ```java
      String message = new String(in_packet.getData(), 0, in_packet.getLength());
      ``` 
  * **Bước 7**: Tạo ra gói tin response.
  * **Bước 8**: Gửi gói tin response.
  * **Bước 8**: Đóng `DatagramSocket`.