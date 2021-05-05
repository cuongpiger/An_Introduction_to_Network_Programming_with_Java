import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class UseFileChooser extends JFrame implements ActionListener {
    private JPanel display_pnl, button_pnl;
    private JLabel surname_lbl, firstname_lbl, mark_lbl;
    private JTextField surname_box, firstname_box, mark_box;
    private JButton open_btn, next_btn;
    private Scanner input;

    public UseFileChooser() {
        setTitle("FileChooser Demo");
        setLayout(new BorderLayout());
        display_pnl = new JPanel();
        display_pnl.setLayout(new GridLayout(3,2));
        surname_lbl = new JLabel("Surname");
        firstname_lbl = new JLabel("First names");
        mark_lbl = new JLabel("Mark");
        surname_box= new JTextField();
        firstname_box = new JTextField();
        mark_box = new JTextField();
        surname_box.setEditable(false);
        firstname_box.setEditable(false);
        mark_box.setEditable(false);
        display_pnl.add(surname_lbl);
        display_pnl.add(surname_box);
        display_pnl.add(firstname_lbl);
        display_pnl.add(firstname_box);
        display_pnl.add(mark_lbl);
        display_pnl.add(mark_box);
        add(display_pnl, BorderLayout.NORTH);
        button_pnl = new JPanel();
        button_pnl.setLayout(new FlowLayout());
        open_btn = new JButton("Open File");
        open_btn.addActionListener(this);
        next_btn = new JButton("Next Record");
        next_btn.addActionListener(this);
        next_btn.setEnabled(false);
        button_pnl.add(open_btn);
        button_pnl.add(next_btn);
        add(button_pnl, BorderLayout.SOUTH);

        addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent event) {
                        if (input != null) // file is opening.
                            input.close();

                        System.exit(0);
                    }
                }
        );
    }

    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == open_btn) {
            try {
                openFile();
            } catch (IOException err) {
                JOptionPane.showMessageDialog(this, "Unable to open file!");
            }
        } else {
            try {
                readRecord();
            } catch (EOFException err) {
                next_btn.setEnabled(false);
                JOptionPane.showMessageDialog(this, "In complete record!\nEnd of file reached.");
            } catch (IOException err) {
                JOptionPane.showMessageDialog(this, "Unable to read file!");
            }
        }
    }

    public void openFile() throws IOException {
        if (input != null) {
            input.close();
            input = null;
        }

        JFileChooser file_chooser = new JFileChooser();
        file_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int selection = file_chooser.showOpenDialog(null);
        if (selection == JFileChooser.CANCEL_OPTION) return;

        File results = file_chooser.getSelectedFile();
        if (results == null || results.getName().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid selection!");
            return;
        }

        input = new Scanner(results);
        readRecord(); // read and display first record
        next_btn.setEnabled(true);
    }

    public void readRecord() throws IOException {
        String surname, firstname, textmark;

        surname_box.setText("");
        firstname_box.setText("");
        mark_box.setText("");

        if (input.hasNext()) {
            surname = input.nextLine();
            surname_box.setText(surname);
        } else {
            JOptionPane.showMessageDialog(this, "End of file reached.");
            next_btn.setEnabled(false);

            return;
        }

        if (!input.hasNext()) throw (new EOFException());

        // otherwise
        firstname = input.nextLine();
        firstname_box.setText(firstname);

        if (!input.hasNext()) throw (new EOFException());

        // otherwise
        textmark = input.nextLine();
        mark_box.setText(textmark);
    }

    public static void main(String[] args) {
        UseFileChooser frame = new UseFileChooser();
        frame.setSize(350, 150);
        frame.setVisible(true);
    }
}
