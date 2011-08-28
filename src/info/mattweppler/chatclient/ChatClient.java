package info.mattweppler.chatclient;

import java.net.*;
import java.io.*;
import java.awt.*;

public class ChatClient extends Frame implements Runnable
{
    private static final long serialVersionUID = 34602517901632082L;
    protected DataInputStream dataInputStream;
    protected DataOutputStream dataOutputStream;
    protected TextArea outputTextArea;
    protected TextField inputTextField;
    protected Thread listenerThread;
    protected boolean stillAlive;
    
    public ChatClient(String title, InputStream inStream, OutputStream outStream)
    {
        super(title);
        dataInputStream = new DataInputStream(new BufferedInputStream(inStream));
        dataOutputStream = new DataOutputStream(new BufferedOutputStream(outStream));
        setLayout(new BorderLayout());
        add("Center", outputTextArea = new TextArea());
        outputTextArea.setEditable(false);
        add("South", inputTextField = new TextField());
        pack();
        //show();
        this.setVisible(true);
        inputTextField.requestFocus();
        listenerThread = new Thread(this);
        listenerThread.start();
    }
    
    public void run()
    {
        try {
            while (this.stillAlive) {
                String line = dataInputStream.readUTF();
                outputTextArea.append(line + "\n");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            listenerThread = null;
            inputTextField.setVisible(false);
            validate();
            try {
                dataOutputStream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    
    public boolean handleEvent(Event e)
    {
        if ((e.target == inputTextField) && (e.id == Event.ACTION_EVENT)) {
            try {
                dataOutputStream.writeUTF((String) e.arg);
                dataOutputStream.flush();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                this.stillAlive = false;
            }
            inputTextField.setText("");
            return true;
        } else if ((e.target == this) && (e.id == Event.WINDOW_DESTROY)) {
            if (listenerThread != null) {
                this.stillAlive = false;
            }
            this.setVisible(false);
            return true;
        }
        return super.handleEvent(e);
    }
    
    public static void main(String[] args) throws IOException
    {
        if (args.length != 2) {
            throw new RuntimeException("Syntax: ChatClient <host> <port>");
        }
        Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
        new ChatClient("Chat " + args[0] + ":" + args[1], socket.getInputStream(), socket.getOutputStream());
    }
}