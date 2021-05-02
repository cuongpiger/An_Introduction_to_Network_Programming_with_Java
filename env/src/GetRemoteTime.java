import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.*;
import java.io.*;
import java.util.*;

public class GetRemoteTime extends JFrame implements ActionListener {
    private JTextField host_input;
    private JTextArea display;
    private JButton time_btn;
    private JButton exit_btn;
    private JPanel button_pnl;
    private static Socket socket = null;

    public GetRemoteTime() {
        host_input = new JTextField(20);
        add(host_input, BorderLayout.NORTH);

        display = new JTextArea(10, 15);
        display.setWrapStyleWord(true);
        display.setLineWrap(true);
        add(new JScrollPane(display), BorderLayout.CENTER);

        button_pnl = new JPanel();
        time_btn = new JButton("Get date and time");
        time_btn.addActionListener(this);
        button_pnl.add(time_btn);

        exit_btn = new JButton("Exit");
        exit_btn.addActionListener(this);
        button_pnl.add(exit_btn);
        add(button_pnl, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == exit_btn) {
            System.exit(0);
        }

        String the_time;
        String host = host_input.getText();
        final int DAYTIME_PORT = 1234;

        try {
            socket = new Socket(host, DAYTIME_PORT);
            Scanner input = new Scanner(socket.getInputStream());
            the_time = input.nextLine();
            display.append("The date/time at " + host + " is " + the_time + "\n");
            host_input.setText("");
        } catch (UnknownHostException err) {
            display.append("==> No such host!\n");
            host_input.setText("");
        } catch (IOException err) {
            display.append(err.toString() + "\n");
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException err) {
                System.out.println("==> Unable to disconnect!");
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) {
        GetRemoteTime frame = new GetRemoteTime();
        frame.setSize(400, 300);
        frame.setVisible(true);
        frame.addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent ev) {
                    // check whether a socket is open
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException err) {
                            System.out.println("==> Unable to close link!");
                            System.exit(1);
                        }
                    }

                    System.exit(0);
                }
            }
        );
    }
}
