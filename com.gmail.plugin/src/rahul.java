

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class rahul {
	public void run1() {
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", "Gmail.jar");
        pb.directory(new File("/home/rahul/gmail/"));
        try {
            Process p = pb.start();
            LogStreamReader lsr = new LogStreamReader(p.getInputStream());
            Thread thread = new Thread(lsr, "LogStreamReader");
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


class LogStreamReader implements Runnable {

    private BufferedReader reader;

    public LogStreamReader(InputStream is) {
        this.reader = new BufferedReader(new InputStreamReader(is));
    }

    public void run() {
        try {
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

}
