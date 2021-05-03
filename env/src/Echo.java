import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class Echo extends JFrame implements ActionListener {
    private JTextField host_input, line_send;
    private JLabel host_prompt, message_prompt;
    private JTextArea received;
    private JButton close_conn;
    private JPanel host_pnl, entry_pnl;
    private final int ECHO = 7;
    private static Socket socket = null;
    private Scanner input;
    private PrintWriter output;

    public Echo() {
        host_pnl = new JPanel();
        host_prompt = new JLabel("Enter hostname: ");
        host_input = new JTextField(20);
        host_input.addActionListener(this);
        host_pnl.add(host_prompt);
        host_pnl.add(host_input);
        add(host_pnl, BorderLayout.NORTH);
        entry_pnl = new JPanel();
        message_prompt = new JLabel("Enter text: ");
        line_send = new JTextField(15);

        // change field to editable when hostname entered
        line_send.setEditable(false);
        line_send.addActionListener(this);

    }

    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == close_conn) {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException err) {
                    System.out.println("==> Unable to disconnect");
                    System.exit(1);
                }

                line_send.setEditable(false);
                host_input.grabFocus();
            }

            return;
        }

        if (ev.getSource() == line_send) {

        }
//
//        try {
//
//        }
    }

    public static void main(String[] args) {
        Echo frame = new Echo();
        frame.setSize(600, 400);
        frame.setVisible(true);
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent we) {
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (IOException err) {
                                System.out.println("==> Unable to disconnect!");
                                System.exit(1);
                            } finally {
                                System.exit(0);
                            }
                        }
                    }
                }
        );
    }
}
