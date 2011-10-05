package info.mattweppler.chatclient;

import info.mattweppler.sharedcomponents.CryptoUtils;
import info.mattweppler.sharedcomponents.Message;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UTFDataFormatException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChatClient implements KeyListener, WindowListener
{
    private JFrame frame;
    protected String username;
    protected DataInputStream dataInputStream;
    protected DataOutputStream dataOutputStream;
    protected ObjectInputStream objectInputStream;
    protected ObjectOutputStream objectOutputStream;
    protected ClientListener listener;
    protected boolean stillAlive;
    protected JTextArea outputTextArea;
    protected JTextArea inputTextArea;   
    
    public ChatClient(String title, String username, InputStream inStream, OutputStream outStream)
    {
        this.username = username; //"-test";
        dataInputStream = new DataInputStream(new BufferedInputStream(inStream));
        dataOutputStream = new DataOutputStream(new BufferedOutputStream(outStream));
        try {
        	objectOutputStream = new ObjectOutputStream(outStream);
        	objectInputStream = new ObjectInputStream(inStream);
        } catch (IOException ioe) {
        	ioe.printStackTrace();
        }
        //TODO - Take the time to do a better user interface.
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(outputTextArea = new JTextArea());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputTextArea = new JTextArea(), BorderLayout.SOUTH);
        frame.setSize(550,450);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width-w)/2;
        int y = (dim.height-h)/2;
        frame.setLocation(x,y);
        frame.setVisible(true);

        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.requestFocus();
        inputTextArea.addKeyListener(this);
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        
        //Client Listener Thread
        this.stillAlive = true;
        listener = new ClientListener("ClientListener", this);
        System.out.println("Created Thread:"+listener);
    }
    
    public void clearInputArea() {
    	inputTextArea.setText(null);
        // remove the newline from the input field.
        inputTextArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
        // output textarea follows the content instead of just staying at the current line.
        outputTextArea.setCaretPosition(outputTextArea.getText().length());
    }
    
    public void sendMessage() {
    	try {
            dataOutputStream.writeUTF(CryptoUtils.messageCryptography(username+": "+(String)inputTextArea.getText(), "ENCRYPT"));
            dataOutputStream.flush();
        } catch (UTFDataFormatException utfdfe) {
        	utfdfe.getMessage();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            this.stillAlive = false;
        } finally {
        	clearInputArea();
        }
    }
    
    public void sendMessageObject() {
        Message messageObject = new Message();
        messageObject.setMessage(username+": "+(String)inputTextArea.getText());
        try {
			objectOutputStream.writeObject(messageObject);
			objectOutputStream.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
        	clearInputArea();
        }
    }
    
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isAltDown()) {
			inputTextArea.getDocument().putProperty("filterNewlines", Boolean.FALSE);
			inputTextArea.append("\n");
			return;
		}
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        	//sendMessage();
        	sendMessageObject();
        }
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
	
    //TODO - Figure out a better way to close the application.
    @Override
    public void windowActivated(WindowEvent arg0) {}

    @Override
    public void windowClosed(WindowEvent arg0) {}

    @Override
    public void windowClosing(WindowEvent arg0) {
        if (listener != null) {
            this.stillAlive = false;
        }
        frame.setVisible(false);
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {}

    @Override
    public void windowDeiconified(WindowEvent arg0) {}

    @Override
    public void windowIconified(WindowEvent arg0) {}

    @Override
    public void windowOpened(WindowEvent arg0) {}

    public static void main(String[] args) throws IOException
    {
    	if (args.length == 0) {
    		System.out.println("Syntax: ChatClient <host> <port> <username>");
    		System.out.println("...using default localhost 1137 anonymous");
    		Socket socket = new Socket("localhost", 1137);
    	    new ChatClient("Chat localhost:1137", "username", socket.getInputStream(), socket.getOutputStream());
    	} else if (args.length != 3) {
		    throw new RuntimeException("Syntax: ChatClient <host> <port> <username>");
    	} else {
		    Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
		    new ChatClient("Chat " + args[0] + ":" + args[1], args[2], socket.getInputStream(), socket.getOutputStream());
    	}
    }
    
}
