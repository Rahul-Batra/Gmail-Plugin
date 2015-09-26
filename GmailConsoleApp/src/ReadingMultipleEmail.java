import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFileChooser;


public class ReadingMultipleEmail {

	public static void reademail(String username, String pass) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	int flag = 0;
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect("imap.gmail.com", username, pass);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            System.out.println("Type the number of emails you want to read starting from the latest :");
    		int emails = 0;
    		try {
    			emails = Integer.parseInt(reader.readLine());
    		} catch (NumberFormatException e) {
    			flag = 1;
    		} catch (IOException e) {
    			flag = 1;
    		}
    		if (flag == 0)
    		{
    		if (emails >0 && emails <10)
    		{
            Message[] msg = inbox.getMessages(inbox.getMessageCount()-(emails-1),inbox.getMessageCount());
            List<Message> list = Arrays.asList(msg);
            Collections.reverse(list);
            msg = (Message[]) list.toArray();
            for(int i=0; i < msg.length ; i++ )
            {Address[] in = msg[i].getFrom();
            for (Address address : in) {
                System.out.println("FROM:" + address.toString());
            }
            MimeMultipart mp = (MimeMultipart) msg[i].getContent();
            MimeBodyPart bp = (MimeBodyPart) mp.getBodyPart(0);
            System.out.println("SENT DATE:" + msg[i].getSentDate());
            System.out.println("SUBJECT:" + msg[i].getSubject());
            System.out.println("CONTENT:" + bp.getContent());
            String contentType = msg[i].getContentType();         
            if (contentType.contains("multipart")) {
            	for (int j = 0; j < mp.getCount(); j++) {
            	    MimeBodyPart part = (MimeBodyPart) mp.getBodyPart(j);
            	    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
            	    	System.out.println("Saving attachment.. ");
            	    	System.out.println("Choose the location.. ");
            	    	 String saveDirectory = "";
            	    	 JFileChooser chooser = new JFileChooser();
                         chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                         int returnVal = chooser.showOpenDialog(null);
                         if(returnVal == JFileChooser.APPROVE_OPTION) {
                         	saveDirectory = chooser.getSelectedFile().getAbsolutePath();
                         	part.saveFile(saveDirectory + File.separator + part.getFileName());
                            System.out.println("File saved.. ");
                         }
                         else System.out.println("Attachment Discarded..");
            	    }
            	}
                
            }
            }
    		}
    		}
        } catch (Exception mex) {
            System.out.println("Connection not available");
        }
		
	}

}
