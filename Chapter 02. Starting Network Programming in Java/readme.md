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
  * **Bước 1**: Tạo ra đối tượng `ServerSocket`
    * Constructor `ServerSocket` cần truyền vào một tham số gọi là **port**, ví dụ:
      ```java
      ServerSocket serversocket = new ServerSocket(1234); // port là 1234
      ```
    * Ở ví dụ trên, server sẽ chờ một kết nối nào đó từ client trên port 1234  
  * **Bước 2**: 