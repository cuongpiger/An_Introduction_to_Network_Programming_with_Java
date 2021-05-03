import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class PortScanner extends JFrame implements ActionListener {
    private JLabel prompt;
    private JTextField host_input;
    private JTextArea report;
    private JButton seek_btn, exit_btn;
    private JPanel host_pnl, button_pnl;
    private static Socket socket = null;

    public PortScanner() {
        host_pnl = new JPanel();
        prompt = new JLabel("Host name: ");
        host_input = new JTextField("rosie", 25);
        host_pnl.add(prompt);
        host_pnl.add(host_input);
        add(host_pnl, BorderLayout.NORTH);
        report = new JTextArea(10, 25);
        add(report, BorderLayout.CENTER);
        button_pnl = new JPanel();
        seek_btn = new JButton("Seek server ports");
        seek_btn.addActionListener(this);
        button_pnl.add(seek_btn);
        exit_btn = new JButton("Exit");
        exit_btn.addActionListener(this);
        button_pnl.add(exit_btn);
        add(button_pnl, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == exit_btn)
            System.exit(0);

        report.setText("");
        String host = host_input.getText();
        try {
            InetAddress theAddress = InetAddress.getByName(host);
            report.append(">> IP address: " + theAddress + "\n");
            for (int i = 1234; i < 1245; i++) {
                try {
                    socket = new Socket(host, i);
                    report.append(">> There is a server on port " + i + ".\n");
                    socket.close();
                } catch (IOException err) { }
            }
        } catch (UnknownHostException uhEx) {
            report.setText("==> Unknown host!");
        }
    }

    public static void main(String[] args) {
        PortScanner frame = new PortScanner();
        frame.setSize(400, 300);
        frame.setVisible(true);
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent event) { // check whether a socket is open…
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (IOException err) {
                                System.out.println("==> Unable to close link!\n");
                                System.exit(1);
                            }
                        }
                        System.exit(0);
                    }
                }
        );
    }
}
