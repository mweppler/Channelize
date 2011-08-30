package info.mattweppler.chatclient;

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
	
	//TODO - need way better error handling through...
	public void run()
	{
		System.out.println(Thread.currentThread());
		try {
			while (client.stillAlive) {
				String line = client.dataInputStream.readUTF();
				client.outputTextArea.append(line + "\n");
			}
			System.out.println("ChatClient Thread is dead...");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			thread = null;
			client.inputTextArea.setVisible(false);
			client.sendButton.setVisible(false);
			try {
				client.dataOutputStream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}