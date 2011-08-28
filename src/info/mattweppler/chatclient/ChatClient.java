package info.mattweppler.chatclient;

import java.net.*;
import java.io.*;
import java.awt.*;

public class ChatClient extends Frame implements Runnable
{
    protected DataInputStream dataInputStream;
    protected DataOutputStream dataOutputStream;
    protected TextArea outputTextArea;
    protected TextField inputTextField;
    protected Thread listenerThread;
    
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
        show();
        inputTextField.requestFocus();
        listenerThread = new Thread(this);
        listenerThread.start();
    }
    
    public void run()
    {
        try {
            while (true) {
                String line = dataInputStream.readUTF();
                outputTextArea.appendText(line + "\n");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            listenerThread = null;
            inputTextField.hide();
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
                listenerThread.stop();
            }
            inputTextField.setText("");
            return true;
        } else if ((e.target == this) && (e.id == Event.WINDOW_DESTROY)) {
            if (listenerThread != null) {
                listenerThread.stop();
            }
            hide();
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