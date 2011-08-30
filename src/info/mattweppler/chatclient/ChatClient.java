package info.mattweppler.chatclient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChatClient implements ActionListener, WindowListener
{
    private JFrame frame;
    private String username;
    protected DataInputStream dataInputStream;
    protected DataOutputStream dataOutputStream;
    protected ClientListener listener;
    protected boolean stillAlive;
    protected JTextArea outputTextArea;
    protected JTextArea inputTextArea;
    protected JButton sendButton;   
    
    public ChatClient(String title, String username, InputStream inStream, OutputStream outStream)
    {
        this.username = username;
        dataInputStream = new DataInputStream(new BufferedInputStream(inStream));
        dataOutputStream = new DataOutputStream(new BufferedOutputStream(outStream));

        //TODO - Take the time to do a better user interface.
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(outputTextArea = new JTextArea());
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(inputTextArea = new JTextArea(1,35));
        panel.add(sendButton = new JButton("Send"));
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);
        frame.setSize(550,450);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
        frame.setLocation(x,y);
        frame.setVisible(true);

        sendButton.addActionListener(this);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.requestFocus();
        outputTextArea.setEditable(false);

        this.stillAlive = true;
        listener = new ClientListener("ClientListener", this);
        System.out.println("Created Thread:"+listener);
    }

    public static void main(String[] args) throws IOException
    {
//      Socket socket = new Socket("localhost", 1137);
//      new ChatClient("Chat localhost:1137", socket.getInputStream(), socket.getOutputStream());

        if (args.length != 3) {
            throw new RuntimeException("Syntax: ChatClient <host> <port> <username>");
        }
        Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
        new ChatClient("Chat " + args[0] + ":" + args[1], args[2], socket.getInputStream(), socket.getOutputStream());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(sendButton)) {
            try {
                dataOutputStream.writeUTF(username+": "+(String)inputTextArea.getText());
                dataOutputStream.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                this.stillAlive = false;
            }
            inputTextArea.setText("");
        }
    }

    //TODO - Figure out a better way to close the application.
    @Override
    public void windowActivated(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosed(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        // TODO Auto-generated method stub
        if (listener != null) {
            this.stillAlive = false;
        }
        frame.setVisible(false);
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowIconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowOpened(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

}
