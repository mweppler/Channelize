package info.mattweppler.chatclient;

import java.util.EventListener;
import java.util.EventObject;

import javax.swing.event.EventListenerList;

public class MessageReceiverEvent extends EventObject
{
	private static final long serialVersionUID = 7107951334383733685L;
	public MessageReceiverEvent(Object source)
	{
		super(source);
	}
}

interface MessageReceiverListener extends EventListener
{
	public void messageEventOccurred(MessageReceiverEvent event);
}

class MessageReceiver
{
	protected EventListenerList listenerList = new EventListenerList();

	public void addEventListener(MessageReceiverListener listener)
	{
		listenerList.add(MessageReceiverListener.class, listener);
	}
	public void removeEventListener(MessageReceiverListener listener)
	{
		listenerList.remove(MessageReceiverListener.class, listener);
	}
	public void fireEvent(MessageReceiverEvent event)
	{
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i = i+2) {
			if (listeners[i] == MessageReceiverListener.class) {
				((MessageReceiverListener) listeners[i+1]).messageEventOccurred(event);
			}
		}
	}
}
