package info.mattweppler.chatclient;

import java.util.EventListener;
import java.util.EventObject;

import javax.swing.event.EventListenerList;

public class MessageSenderEvent extends EventObject
{
	private static final long serialVersionUID = 7107951334383733685L;
	public MessageSenderEvent(Object source)
	{
		super(source);
	}
}

interface MessageSenderListener extends EventListener
{
	public void messageEventOccurred(MessageSenderEvent event);
}

class MessageSender
{
	protected EventListenerList listenerList = new EventListenerList();

	public void addEventListener(MessageSenderListener listener)
	{
		listenerList.add(MessageSenderListener.class, listener);
	}
	public void removeEventListener(MessageSenderListener listener)
	{
		listenerList.remove(MessageSenderListener.class, listener);
	}
	public void fireEvent(MessageSenderEvent event)
	{
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i = i+2) {
			if (listeners[i] == MessageSenderListener.class) {
				((MessageSenderListener) listeners[i+1]).messageEventOccurred(event);
			}
		}
	}
}
