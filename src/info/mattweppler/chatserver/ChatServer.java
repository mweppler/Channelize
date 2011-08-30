package info.mattweppler.chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer
{
    public ChatServer(int port) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Listening on port:"+port+" for incoming connections.");
        while (true) { // when should this be ended?
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection from: " + clientSocket.getInetAddress());
            ChatHandler chatHandler = new ChatHandler(clientSocket);
            chatHandler.stillAlive = true;
            chatHandler.start();
        }
    }
    
    public static void main(String[] args) throws IOException
    {
//    	new ChatServer(1137);
    	
    	if (args.length != 1) {
            throw new RuntimeException("Syntax: ChatServer <port>");
        }
        new ChatServer(Integer.parseInt(args[0]));
    }
}