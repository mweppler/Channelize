package info.mattweppler.chatserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;

public class ChatHandler extends Thread
{
    protected Socket socket;
    protected DataInputStream dataInputStream;
    protected DataOutputStream dataOutputStream;
    protected static Vector<ChatHandler> handlers = new Vector<ChatHandler>();
    protected boolean stillAlive;
    
    public ChatHandler(Socket socket) throws IOException
    {
        this.socket = socket;
        dataInputStream = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
        dataOutputStream = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
    }
    
    public void run()
    {
        try {
            handlers.addElement(this);
            while (this.stillAlive) {
                String message = dataInputStream.readUTF();
                broadcast(message);
            }
        } catch (EOFException eofe) {
        	eofe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
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
                        chatHandler.dataOutputStream.writeUTF(message);
                    }
                    chatHandler.dataOutputStream.flush();
                } catch (IOException ioe) {
                	ioe.printStackTrace();
                    chatHandler.stillAlive = false;
                }
            }
        }
    }
}