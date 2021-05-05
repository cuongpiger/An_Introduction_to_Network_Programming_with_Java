import java.io.*;
import java.util.*;

public class FileMethods {
    public static void main(String[] args) throws IOException {
        String file_name;
        Scanner input = new Scanner(System.in);
        System.out.print(">> Press <Enter> to quit: ");
        file_name = input.nextLine().trim();

        while (!file_name.isEmpty()) {
            File file_dir = new File(file_name);

            if (!file_dir.exists()) { // nếu file này chưa tồn tại
                System.out.println(">> " + file_name + " does not exist.");
                break;
            }

            System.out.print(">> " + file_name + " is a ");
            if (file_dir.isFile()) { // kiểm tra file có phải là file hay ko hay là thư mục hoặc qq j khác
                System.out.println("file.");
            } else {
                System.out.println("directory.");
            }

            System.out.println(">> Can read " + file_name + ": " + file_dir.canRead());
            System.out.println(">> Can write " + file_name + ": " + file_dir.canWrite());

            if (file_dir.isDirectory()) { // nếu là thư mục thì tiến hành đọc tên các file bên trong nó
                System.out.println("   >> Content: ");
                String[] file_lst = file_dir.list(); // lấy danh sách các file bên trong nó
                for (var file : file_lst) {
                    System.out.println("      >> " + file);
                }
            } else {
                System.out.println(">> Size of " + file_name + " is " + file_dir.length() + " bytes");
            }

            System.out.print("Enter name of next file/directory: ");
            file_name = input.nextLine().trim();
        }

        input.close();
    }
}
