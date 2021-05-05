import java.io.*;
import java.util.*;

public class FileTest3 {
    public static void main(String[] args) throws IOException {
        int mark, total = 0, count = 0;
        String file_name = "./data/test2.txt";
        Scanner input = new Scanner(new File(file_name));

        while (input.hasNext()) { // kiểm tra cuối file hay chưa
            mark = input.nextInt();
            total += mark;
            count += 1;
        }

        input.close();

        System.out.println(">> Mean = " + (float) (total / count));
    }
}
