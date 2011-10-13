package info.mattweppler.chatclient;

import info.mattweppler.sharedcomponents.CryptoUtils;
import info.mattweppler.sharedcomponents.Message;

import java.io.IOException;

public class ClientListener implements Runnable
{
	public Thread thread;
	public ChatClient client;
	private Message messageObject; 
	
	public ClientListener(String name, ChatClient cc)
	{
		client = cc;
		thread = new Thread(this, name);
		thread.start();
	}

	public void run()
	{
		System.out.println(Thread.currentThread());
		try {
			while (client.stillAlive) {
				if (true) {
					messageObject = (Message) client.objectInputStream.readObject();
					client.receiveMessageObject(messageObject);
				} else {
					client.receiveMessage(CryptoUtils.messageCryptography(client.dataInputStream.readUTF(),"DECRYPT"));
				}
			}
		} catch (IOException ioe) {
			if (ioe.getMessage().equals("Connection reset")) {
				client.outputTextArea.append("You have been disconnected from the server."+"\n");
				// output textarea follows the content instead of just staying at the current line.
				client.outputTextArea.setCaretPosition(client.outputTextArea.getText().length());
				System.out.println("You have been disconnected from the server.");
			} else {
				ioe.printStackTrace();
			}
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		} finally {
			thread = null;
			client.inputTextArea.setVisible(false);
			try {
				client.dataOutputStream.close();
			} catch (IOException ioe) {
				System.out.println("ClientListener.class on line: "+ioe.getMessage());
				ioe.printStackTrace();
			}
		}
	}
}