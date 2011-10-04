package info.mattweppler.chatclient;

import info.mattweppler.sharedcomponents.CryptoUtils;

import java.awt.Color;
import java.io.IOException;

public class ClientListener implements Runnable
{
	public Thread thread;
	public ChatClient client; 
	
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
				String line = CryptoUtils.messageCryptography(client.dataInputStream.readUTF(),"DECRYPT");
				// tried to distinguish local user text from remote user. 
				if (line.indexOf(client.username) != -1) { //Local User
					client.outputTextArea.setBackground(new Color(240, 248, 255));
				} else { //Remote User
					client.outputTextArea.setBackground(new Color(211, 211, 211));
				}
				//System.out.println("Timestamp is: "+line.substring(line.lastIndexOf("\n")+2));
				client.outputTextArea.append(line + "\n");
				// output textarea follows the content instead of just staying at the current line.
				client.outputTextArea.setCaretPosition(client.outputTextArea.getText().length());
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