package info.mattweppler.chatserver;

import info.mattweppler.sharedcomponents.CryptoUtils;
import info.mattweppler.sharedcomponents.Message;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Vector;

public class ChatHandler extends Thread
{
	private static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss"; 
	protected Socket socket;
    protected DataInputStream dataInputStream;
    protected DataOutputStream dataOutputStream;
    protected ObjectInputStream objectInputStream;
    protected ObjectOutputStream objectOutputStream;
    protected static Vector<ChatHandler> handlers = new Vector<ChatHandler>();
    protected boolean stillAlive;
    
    public ChatHandler(Socket socket) throws IOException
    {
        this.socket = socket;
        this.setName(new String(socket.getInetAddress().toString()));
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        Calendar cal = Calendar.getInstance();
        System.out.println("Connection from: "+this.getName()+" at "+sdf.format(cal.getTime()));
        dataInputStream = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
        dataOutputStream = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
    	objectInputStream = new ObjectInputStream(this.socket.getInputStream());
    	objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
    }
    
    public void run()
    {
        try {
            handlers.addElement(this);
            while (this.stillAlive) {
                //String message = dataInputStream.readUTF();
                Message message = (Message) objectInputStream.readObject();
                broadcast(message);
            }
        } catch (SocketException se) {
        	if (se.getMessage().equals("Connection reset")) {
        		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        		Calendar cal = Calendar.getInstance();
        		System.out.println(this.getName()+" has disconnected at "+sdf.format(cal.getTime()));
        	}
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
        	cnfe.printStackTrace();
        } finally {
            handlers.removeElement(this);
            try {
                socket.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    protected static void broadcast(String message)
    {
        synchronized (handlers) {
            Enumeration<ChatHandler> enumeration = handlers.elements();
            while (enumeration.hasMoreElements()) {
                ChatHandler chatHandler = (ChatHandler) enumeration.nextElement();
                try {
                    synchronized (chatHandler.dataOutputStream) {
                    	Calendar cal = Calendar.getInstance();
                    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
                    	System.out.println("Encrypted Message at server: "+message);
                    	String decryptedMessage = CryptoUtils.messageCryptography(message, "DECRYPT");
                    	String encryptedMessage = CryptoUtils.messageCryptography(decryptedMessage+"\n"+sdf.format(cal.getTime()),"ENCRYPT");
                        chatHandler.dataOutputStream.writeUTF(encryptedMessage);
                    }
                    chatHandler.dataOutputStream.flush();
                } catch (IOException ioe) {
                	ioe.printStackTrace();
                    chatHandler.stillAlive = false;
                }
            }
        }
    }
    
    protected static void broadcast(Message message)
    {
        synchronized (handlers) {
            Enumeration<ChatHandler> enumeration = handlers.elements();
            while (enumeration.hasMoreElements()) {
                ChatHandler chatHandler = (ChatHandler) enumeration.nextElement();
                try {
                    synchronized (chatHandler.objectOutputStream) {
                    	Calendar cal = Calendar.getInstance();
                    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
                    	message.setTimestamp(sdf.format(cal.getTime()));
                    	System.out.println("Encrypted Message at server: "+message.toString());
                    	chatHandler.objectOutputStream.writeObject(message);
                    }
                    chatHandler.objectOutputStream.flush();
                } catch (IOException ioe) {
                	ioe.printStackTrace();
                    chatHandler.stillAlive = false;
                }
            }
        }
    }
    
}