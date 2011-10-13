package info.mattweppler.sharedcomponents;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Message implements Serializable
{
	private static final long serialVersionUID = 7299482643091594818L;
	private int id;
	//private Object message;
	private String message;
	private int receiverId;
	private String receiverName;
	private int senderId;
	private String senderName;
	private String timestamp;
	
	public Message() {
		
	}

	public int getId() {
		return id;
	}
	//public Object getMessage() {
	//	return message;
	//}
	public String getMessage() {
		return message;
	}
	public int getReceiverId() {
		return receiverId;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public int getSenderId() {
		return senderId;
	}
	public String getSenderName() {
		return senderName;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setId(int id) {
		this.id = id;
	}
	//public void setMessage(Object message) {
	//	this.message = message;
	//}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", message=" + message + ", receiverId="
				+ receiverId + ", receiverName=" + receiverName + ", senderId="
				+ senderId + ", senderName=" + senderName + ", timestamp="
				+ timestamp + "]";
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