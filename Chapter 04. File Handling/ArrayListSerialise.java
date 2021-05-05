import java.io.*;
import java.util.*;

class Personnel implements Serializable {
    private long payroll_num;
    private String surname;
    private String firstname;

    public Personnel(long p, String s, String f) {
        payroll_num = p;
        surname = s;
        firstname = f;
    }

    public long get_payroll_num() {
        return payroll_num;
    }

    public String get_surname() {
        return surname;
    }

    public String get_firstname() {
        return firstname;
    }

    public void set_surname(String s) {
        surname = s;
    }
}

public class ArrayListSerialise {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String file_name = "./data/personnel_list.dat";
        ObjectOutputStream out_stream = new ObjectOutputStream(new FileOutputStream(file_name));
        ArrayList<Personnel> staff_list_out = new ArrayList<>();
        ArrayList<Personnel> staff_list_in = new ArrayList<>();

        Personnel[] staff = {
                new Personnel(123456, "Duong", "Cuong"),
                new Personnel(234567, "Nguyen", "Que"),
                new Personnel(345678, "Pham", "Diem")
        };

        for (var ob : staff) staff_list_out.add(ob);
        out_stream.writeObject(staff_list_out);
        out_stream.close();

        ObjectInputStream in_stream = new ObjectInputStream(new FileInputStream(file_name));
        int staff_count = 0;

        try {
            staff_list_in = (ArrayList<Personnel>) in_stream.readObject();
            for (var person : staff_list_in) {
                System.out.println(">> Staff member: " + ++staff_count);
                System.out.println("   >> Payroll number: " + person.get_payroll_num());
                System.out.println("   >> Surname: " + person.get_surname());
                System.out.println("   >> Firstname: " + person.get_firstname());
            }

            System.out.println("\n");
        } catch (EOFException err) {
            System.out.println("--> End of file.");
            in_stream.close();
        }
    }
}
