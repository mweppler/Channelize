package info.mattweppler.chatserver;

import java.net.*;
import java.io.*;
import java.util.*;

public class ChatHandler extends Thread
{
    protected Socket socket;
    protected DataInputStream dataInputStream;
    protected DataOutputStream dataOutputStream;
    protected static Vector handlers = new Vector();
    
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
            while (true) {
                String message = dataInputStream.readUTF();
                broadcast(message);
            }
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
        synchronized(handlers)
        {
            Enumeration enumeration = handlers.elements();
            while (enumeration.hasMoreElements()) {
                ChatHandler chatHandler = (ChatHandler) enumeration.nextElement();
                try {
                    synchronized(chatHandler.dataOutputStream)
                    {
                        chatHandler.dataOutputStream.writeUTF(message);
                    }
                    chatHandler.dataOutputStream.flush();
                } catch (IOException ioe) {
                    chatHandler.stop();
                }
            }
        }
    }
}