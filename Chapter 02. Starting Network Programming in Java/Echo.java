import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class Echo extends JFrame implements ActionListener {
    private static JTextField host_input, line_send;
    private JLabel host_prompt, message_prompt;
    private static JTextArea received;
    private static JButton close_conn;
    private JPanel host_pnl, entry_pnl;
    private final static int ECHO = 1234;
    private static Socket socket = null;
    private static String host = "";
    private static Scanner input;
    private static PrintWriter output;

    public Echo() {
        host_pnl = new JPanel();
        host_prompt = new JLabel("Enter hostname: ");
        host_input = new JTextField(20);
        host_input.addActionListener(this);
        close_conn = new JButton("Close connection");
        close_conn.addActionListener(this);
        close_conn.setEnabled(false); // disable this button
        host_pnl.add(host_prompt);
        host_pnl.add(host_input);
        host_pnl.add(close_conn);
        add(host_pnl, BorderLayout.NORTH);

        entry_pnl = new JPanel();
        message_prompt = new JLabel("Enter text: ");
        line_send = new JTextField(40);
        line_send.addActionListener(this);
        line_send.setEditable(false); // change field to editable when hostname entered
        entry_pnl.add(message_prompt);
        entry_pnl.add(line_send);
        add(entry_pnl, BorderLayout.AFTER_LAST_LINE);

        received = new JTextArea(10, 15);
        received.setWrapStyleWord(true);
        received.setLineWrap(true);
        add(new JScrollPane(received), BorderLayout.CENTER);
    }

    private static void connectServer() {
        if (socket == null) {
            try {
                socket = new Socket(host, ECHO);
            } catch (UnknownHostException err) {
                received.append("⛔ No such host!\n");
                prepareSocket();
            } catch (IOException err) {
                received.append("⛔" + err.toString() + "\n");
            }

            System.out.println("Success!");
        }
    }

    private static void prepareSocket() {
        host_input.setEditable(false); // not allowed edit this component
        close_conn.setEnabled(true); // allow clicking on this button
        line_send.setText("");
        line_send.setEditable(true); // allow edit this component
        line_send.grabFocus(); // focus the cursor to this component
        received.setText("");

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException err) {
                received.append("⛔ Unable to disconnect!\n");
                System.exit(1);
            } finally {
                socket = null;
            }
        }
    }

    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == host_input) {
            host = host_input.getText().trim();

            if (!host.isEmpty()) {
                prepareSocket();
                connectServer();
            }

            return;
        }

        if (ev.getSource() == close_conn) {
            if (socket != null) {
                try {
                    System.out.println("Closing the connection...");
                    socket.close();
                } catch (IOException err) {
                    received.append("⛔ Unable to disconnect!\n");
                    System.exit(1);
                } finally {
                    socket = null;
                }

                close_conn.setEnabled(false);
                received.setText("");
                line_send.setText("");
                line_send.setEditable(false);
                host_input.setText("");
                host_input.setEditable(true);
                host_input.grabFocus();
            }

            return;
        }

        if (ev.getSource() == line_send) {
            if (socket != null) {
                String message = line_send.getText().trim();

                if (message.isEmpty()) return;

                try {
                    input = new Scanner(socket.getInputStream());
                    output = new PrintWriter(socket.getOutputStream(), true);
                    output.println(message);
                    String response = input.nextLine();
                    received.append(response + "\n");
                } catch (IOException err) {
                    received.append("⛔" + err.toString() + "\n");
                    System.exit(1);
                } finally {
                    line_send.setText("");
                    line_send.grabFocus();
                }
            }

            return;
        }
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
