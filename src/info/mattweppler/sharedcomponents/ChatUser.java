package info.mattweppler.sharedcomponents;

//TODO - implement some type of user scheme
public class ChatUser
{
	private int id;
	private String username;
	private String hashedPassword;
	
	public ChatUser()
	{
		
	}

	public int getId()
	{
		return id;
	}

	public String getUsername()
	{
		return username;
	}

	public String getHashedPassword()
	{
		return hashedPassword;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public void setHashedPassword(String hashedPassword)
	{
		this.hashedPassword = hashedPassword;
	}

	@Override
	public String toString()
	{
		return "ChatUser [id=" + id + ", username=" + username
				+ ", hashedPassword=" + hashedPassword + "]";
	}
	
}
