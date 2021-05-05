import java.io.*;
import java.util.*;

public class RanFile1 {
    private static final int REC_SIZE = 48;
    private static final int SURNAME_SIZE = 15;
    private static final int NUM_INITS = 3;
    private static long acct_num = 0;
    private static String surname, initials;
    private static float balance;

    public static void writeRecord(RandomAccessFile file) throws IOException {
        long file_pos = (acct_num - 1) * REC_SIZE;
        file.seek(file_pos); // move cursor
        file.writeLong(acct_num);
        writeString(file, surname, SURNAME_SIZE);
        writeString(file, initials, NUM_INITS);
        file.writeFloat(balance);
    }

    public static void writeString(RandomAccessFile file, String text, int fixed_size) throws IOException {
        int size = text.length();

        if (size <= fixed_size) {
            file.writeChars(text);
            for (int i = size; i < fixed_size; ++i) {
                file.writeChar(' ');
            }
        } else {
            file.writeChars(text.substring(0, fixed_size));
        }
    }

    public static void showRecords(RandomAccessFile file) throws IOException {
        long num_records = file.length() / REC_SIZE;
        file.seek(0);

        for (int i = 0; i < num_records; ++i) {
            acct_num = file.readLong();
            surname = readString(file, SURNAME_SIZE);
            initials = readString(file, NUM_INITS);
            balance = file.readFloat();

            System.out.printf("--> " + acct_num + "   " + surname + "   " + initials + "   " + "%.2f %n", balance);
        }
    }

    public static String readString(RandomAccessFile file, int fixed_size) throws IOException {
        String value = "";

        for (int i = 0; i < fixed_size; ++i) value += file.readChar();
        return value.trim();
    }

    public static void main(String[] args) throws IOException {
        String file_name = "./data/accounts.dat";
        RandomAccessFile ran_accts = new RandomAccessFile(file_name, "rw");
        Scanner input = new Scanner(System.in);
        String reply = "y";

        do {
            System.out.println(">> Account number " + ++acct_num);
            System.out.print("   >> Surname: ");
            surname = input.nextLine();
            System.out.print("   >> Initial(s): ");
            initials = input.nextLine();
            System.out.print("   >> Balance: ");
            balance = input.nextFloat();

            input.nextLine(); // loại bỏ dấu xuống dòng

            writeRecord(ran_accts);
            System.out.print(">> Do you wish to do this again [Y/N]: ");
            reply = input.nextLine().trim().toLowerCase();
        } while (reply.equals("y"));

        System.out.println();
        showRecords(ran_accts);
        ran_accts.close();
}
