import java.io.*;

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

public class Serialise {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String file_name = "./data/personnel.dat";
        ObjectOutputStream out_stream = new ObjectOutputStream(new FileOutputStream(file_name));
        Personnel[] staff = {
                new Personnel(123456, "Duong", "Cuong"),
                new Personnel(234567, "Nguyen", "Que"),
                new Personnel(345678, "Huynh", "Hanh")
        };

        for (int i = 0; i < staff.length; ++i) {
            out_stream.writeObject(staff[i]);
        }

        out_stream.close();
        ObjectInputStream in_stream = new ObjectInputStream(new FileInputStream(file_name));

        int staff_count = 0;

        try {
            do {
                Personnel person = (Personnel) in_stream.readObject();
                staff_count += 1;

                System.out.println(">> Staff member " + staff_count);
                System.out.println("   >> Payroll number: " + person.get_payroll_num());
                System.out.println("   >> Surname: " + person.get_surname());
                System.out.println("   >> Firstname: " + person.get_firstname());
            } while (true);
        } catch (EOFException err) {
            System.out.println("--> End of file");
            in_stream.close();
        }
    }
}
