package info.mattweppler.chatserver;

import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer
{
    public ChatServer(int port) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted from: " + clientSocket.getInetAddress());
            ChatHandler chatHandler = new ChatHandler(clientSocket);
            chatHandler.start();
        }
    }
    
    public static void main(String[] args) throws IOException
    {
        if (args.length != 1) {
            throw new RuntimeException("Syntax: ChatServer <port>");
        }
        new ChatServer(Integer.parseInt(args[0]));
    }
}