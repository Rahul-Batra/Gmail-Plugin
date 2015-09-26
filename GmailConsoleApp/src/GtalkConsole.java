import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
public class GtalkConsole implements MessageListener
{	XMPPConnection conn;
	static GtalkConsole i = new GtalkConsole();
	String msg;	
	static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	public void LogIn(String usrnme, String pwd) throws XMPPException
	{
		ConnectionConfiguration cnfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
		conn = new XMPPConnection(cnfig);
		conn.connect();
		conn.login(usrnme, pwd);
	}
	public void sendMsg(String msg, String to) throws XMPPException
	{
		Chat chat = conn.getChatManager().createChat(to, this);
		chat.sendMessage(msg);
	}	
	public void showMenu() {
	      System.out.println("\n"+"Please select one of the following menu.");
	      System.out.println("1. List of Friends");
	      System.out.println("2. Send Message");
	      System.out.println("3. Send Email");
	      System.out.println("4. Read Inbox");
	      System.out.println("5. LOGOUT");
	      System.out.print("Your choice [1-5]: ");
	   }
	public void displayfrndslist()
	{
		Roster rster = conn.getRoster();
		Collection<RosterEntry> records = rster.getEntries();
		System.out.println("\nTotal friends: "+records.size());
		int i = 1;
		String status = null;
		for(RosterEntry r:records)
		{
		Presence presence = rster.getPresence(r.getUser());
		if ((presence != null)){
		if(rster.getPresence(r.getUser()).toString().contains("away"))
		status = "idle";
		else if (rster.getPresence(r.getUser()).toString().contains("unavailable"))
		status = "offline";
		else if (rster.getPresence(r.getUser()).toString().contains("dnd"))
		status = "busy";
		else
		status = "available";
		System.out.println("(#" + i + ")"+r.getUser()+"---"+status);
		i++;
		}
		}
	}
	public void sendMessage() throws IOException, XMPPException {
		int flag = 0;
		System.out.println("..........");
		System.out.println("Type the number of users to communicate with--");
		int users = 0;
		try {
			users = Integer.parseInt(reader.readLine());
		} catch (NumberFormatException e) {
			flag = 1;
		} catch (IOException e) {
			flag = 1;
		}
		if (flag == 0)
		{
		if (users >0 && users <10)
		{
		String[] sendto = new String[users];
		System.out.println("whom do u want to communicate? -enter their complete mailing addresses");
		for(int n=0; n<users; n++)
		{
		try {
			sendto[n] = reader.readLine();
		} catch (IOException e) {
			flag = 1;
		}
		if(!sendto[n].contains("@")) 
			flag=1;
		}
		if(flag == 0)
		{
		System.out.println("Every message will be broadcast to: ");
		for(int k=0; k<users; k++) 
	    {	String[] src= sendto[k].split("@");
	    	String var= src[0];
	        System.out.println(var+"  ");
	    }       
		System.out.println("\n"+"Type the message");
		System.out.println("..........\n");
		while(!(msg=reader.readLine()).equals("exit"))
		{
			for (int j = 0; j < sendto.length; j++) {   
			i.sendMsg(msg,sendto[j]);
		}			
		}
		}
		} 
		}
	}
	public void disconnect()
	{
		conn.disconnect();
	}
	public void processMessage(Chat chat, Message msg) 
	{
		if((msg.getType()==Message.Type.chat)&&(msg.getBody() != null))
		 System.out.println(chat.getParticipant() + " says: " + msg.getBody());
	}
	public static void main(String args[]) throws XMPPException, IOException
	{	
		SendFileEmail email = new SendFileEmail();
		ReadingMultipleEmail inbox = new ReadingMultipleEmail(); 
		System.out.println("-----");
        System.out.println("Login information:");
        System.out.print("Enter your username: ");
        String username = reader.readLine();
        System.out.print("Enter your password: ");
        String pass = reader.readLine();
    	try{
    		i.LogIn(username, pass);
		} catch(Exception e)
		{
			System.out.println("Invalid credentials");
			GtalkConsole.main(null);
		}
		try {	
		i.showMenu();
        String data = null;
        menu:
        while((data = reader.readLine().trim()) != null) {
            if (!Character.isDigit(data.charAt(0))) {
               System.out.println("Invalid input.Only 1-5 is allowed !");
               i.showMenu();
               continue;
            }
            int choice = Integer.parseInt(data);
            if ((choice != 1) && (choice != 2) && (choice != 3) && (choice != 4) && (choice != 5)) {
               System.out.println("Invalid input.Only 1-5 is allowed !");
               i.showMenu();
               continue;
            }
            switch (choice) {
               case 1: i.displayfrndslist();
                       i.showMenu();
                       continue menu;
               case 2: i.sendMessage();
                       i.showMenu();
                       continue menu;
               case 3: email.sendemail(username,pass);
               		   i.showMenu();
               		   continue menu;
               case 4: ReadingMultipleEmail.reademail(username,pass);
               		   i.showMenu();
               		   continue menu;
               case 5: GtalkConsole.main(null);
               default: break menu;
            }
         }
         i.disconnect();
      } catch (XMPPException e) {
          if (e.getXMPPError() != null) {
              System.err.println("ERROR-CODE : " + e.getXMPPError().getCode());
              System.err.println("ERROR-CONDITION : " + e.getXMPPError().getCondition());
              System.err.println("ERROR-MESSAGE : " + e.getXMPPError().getMessage());
              System.err.println("ERROR-TYPE : " + e.getXMPPError().getType());
           }
           i.disconnect();
         }catch (Exception e) {
        System.err.println(e.getMessage());
        i.disconnect();
      }
  }
}