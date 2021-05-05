import java.io.*;
import java.util.*;

public class RanFile2 {
    private static final int REC_SIZE = 48;
    private static final int SURNAME_SIZE = 15;
    private static final int NUM_INITS = 3;
    private static long acct_num = 0;
    private static String surname, initials;
    private static float balance;

    public static void showRecord(RandomAccessFile file) throws IOException {
        long num_records = file.length() / REC_SIZE;
        file.seek((acct_num - 1) * REC_SIZE);

        acct_num = file.readLong();
        surname = readString(file, SURNAME_SIZE);
        initials = readString(file, NUM_INITS);
        balance = file.readFloat();

        System.out.printf("--> " + acct_num + "   " + surname + "   " + initials + "   " + "%.2f %n", balance);
    }

    public static String readString(RandomAccessFile file, int fixed_size) throws IOException {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < fixed_size; ++i) buffer.append(file.readChar());
        return buffer.toString();
    }

    public static void main(String[] args) throws IOException {
        String file_name = "./data/accounts.dat";
        Scanner input = new Scanner(System.in);
        RandomAccessFile ran_accts = new RandomAccessFile(file_name, "rw");
        long num_records = ran_accts.length() / REC_SIZE;
        String reply;
        long current_pos;

        do {
            System.out.print(">> Enter account number: ");
            acct_num = input.nextLong();

            while ((acct_num < 1) || (acct_num > num_records)) {
                System.out.println("==> Invalid number!");
                System.out.print(">> Enter account number: ");
                acct_num = input.nextLong();
            }

            showRecord(ran_accts);
            System.out.print(">> Enter new balance: ");
            balance = input.nextFloat();
            input.nextLine();

            current_pos = ran_accts.getFilePointer();
            ran_accts.seek(current_pos - 4); // float tốn 4 bytes vùng nhớ
            ran_accts.writeFloat(balance);
            System.out.print(">> Modify another balance [Y/N]: ");
            reply = input.nextLine().trim().toLowerCase();
        } while (reply.equals("y"));

        ran_accts.close();
    }
}
