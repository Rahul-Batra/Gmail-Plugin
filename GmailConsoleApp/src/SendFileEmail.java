import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Authenticator;
import javax.swing.JFileChooser;

public class SendFileEmail
{	GtalkConsole i = new GtalkConsole();
   public void sendemail(String username, final String password) 
   {	int flag = 0;
	   BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	    System.out.println("..........");
		System.out.println("Type the number of recipients--");
		int users = 0;
		try {
			users = Integer.parseInt(reader.readLine());
		} catch (NumberFormatException e1) {
			flag=1;
		} catch (IOException e1) {
			flag=1;
		}
		if( flag == 0)
		{
		String[] receiver = new String[users];
		if (users>0 && users <10)
		{
		System.out.println("Type the recipients complete mailing addresses--");
		for(int n=0; n<users; n++)
		{
			try {
				receiver[n] = reader.readLine();
			} catch (IOException e) {
				flag=1;
			}
			if(!receiver[n].contains("@"))
				flag=1;
		}
		if (flag == 0)
		{
		System.out.println("Type the number of ccrecipients--");
		try {
			users = Integer.parseInt(reader.readLine());
		} catch (NumberFormatException e1) {
			flag=1;
		} catch (IOException e1) {
			flag=1;
		}
		if (flag == 0)
		{
		String[] ccreceiver = new String[users];
		if(users>0 && users <10)
		{
		System.out.println("Type the ccrecipients complete mailing addresses--");
		for(int n=0; n<users; n++)
		{
			try {
				ccreceiver[n] = reader.readLine();
			} catch (IOException e) {
				flag=1;
			}
			if(!ccreceiver[n].contains("@"))
				flag=1;
		}
		}
		if (flag == 0)
		{
	      final String sender = username;
	      String hostserver = "smtp.gmail.com";
	      Properties prop = System.getProperties();
	      prop.setProperty("mail.smtp.host", hostserver);
	      prop.setProperty("mail.smtp.port", "465");
	      prop.setProperty("mail.smtp.auth", "true");
	      prop.put("mail.debug", "false"); 
	      prop.put("mail.smtp.ssl.enable", "true");
	      prop.setProperty("mail.smtp.user", sender);
	      class Auth extends Authenticator {   
	          @Override  
	          protected PasswordAuthentication getPasswordAuthentication() {  
	              return  new PasswordAuthentication(sender, password);    
	          }  
	      }  
	      Session sn = Session.getInstance(prop,new Auth()); ;
	      try{
	         MimeMessage msg = new MimeMessage(sn);
	         msg.setFrom(new InternetAddress(sender));
	         InternetAddress[] receiverAddresses = new InternetAddress[receiver.length];  
	         for (int i = 0; i < receiver.length; i++) {  
	        	 receiverAddresses[i] = new InternetAddress(receiver[i]);  
	         }  
	         msg.addRecipients(Message.RecipientType.TO,receiverAddresses);
	         InternetAddress[] ccAddresses = new InternetAddress[ccreceiver.length];  
	            for (int j = 0; j < ccreceiver.length; j++) {  
	                ccAddresses[j] = new InternetAddress(ccreceiver[j]);  
	            }  
	         msg.addRecipients(Message.RecipientType.CC, ccAddresses);
	         System.out.println("Type the subject of the mail--");
	         String sub = null;
			try {
				sub = reader.readLine();
			} catch (IOException e) {
				sub="";
			}
	         msg.setSubject(sub);
	         BodyPart msgbody = new MimeBodyPart();
	         System.out.println("Type the content of the mail--");
	         String body = null;
			try {
				body = reader.readLine();
			} catch (IOException e) {
				 body="";
			}
	         msgbody.setText(body);
	         Multipart mpart = new MimeMultipart();
	         mpart.addBodyPart(msgbody);
	         msgbody = new MimeBodyPart();
	         System.out.println("Select an attachment--");
	         String fname = null;
	         JFileChooser chooser = new JFileChooser();	   
	         int returnVal = chooser.showOpenDialog(null);
	         if(returnVal == JFileChooser.APPROVE_OPTION) {
	         fname = chooser.getSelectedFile().getAbsolutePath();
	         System.out.println("Transferring file..");
	         DataSource src = new FileDataSource(fname);
	         msgbody.setDataHandler(new DataHandler(src));
	         msgbody.setFileName(fname);
	         mpart.addBodyPart(msgbody);
	         }
	         else System.out.println("Attachment not selected..");
	         msg.setContent(mpart);
	         Transport.send(msg);
	         System.out.println("Email Delivered..");
	         }catch (MessagingException e) {
	        	 System.out.println("Connection not available");
	         }
		}
		}
		} 
		} 
		}
   	}
}