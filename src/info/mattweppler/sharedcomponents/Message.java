package info.mattweppler.sharedcomponents;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Message implements Serializable
{
	private static final long serialVersionUID = 7299482643091594818L;
	private int id;
	private int senderId;
	private int receiverId;
	//private Object message;
	private String message;
	private String timestamp;
	
	public Message() {
		
	}
	
	public int getId() {
		return id;
	}
	public int getSenderId() {
		return senderId;
	}
	public int getReceiverId() {
		return receiverId;
	}
	//public Object getMessage() {
	//	return message;
	//}
	public String getMessage() {
		return message;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}
	//public void setMessage(Object message) {
	//	this.message = message;
	//}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString() {
		return "Message [id=" + id + ", senderId=" + senderId + ", receiverId="
				+ receiverId + ", timestamp=" + timestamp + ", message="
				+ message + "]";
	}
	
	public static byte[] getBytes(Object obj) throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		ObjectOutputStream oos = new ObjectOutputStream(bos); 
		oos.writeObject(obj);
		oos.flush(); 
		oos.close(); 
		bos.close();
		byte[] data = bos.toByteArray();
		return data;
	}
	
}
