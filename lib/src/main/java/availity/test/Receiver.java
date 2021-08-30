package availity.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class Receiver {	
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	private static final char START_CHAR = '\u000B';
	private static final char END_CHAR = '\u001C';
	private static final char END_TWO = '\r';
	
	
	public Receiver(InetAddress serverAddress, int serverPort) throws Exception {
	    this.socket = new Socket(serverAddress, serverPort);
	    out = new PrintWriter(socket.getOutputStream(), true);
	    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    socket.setSoTimeout(10000);
	}  // end constructor

	
    public void send(String msg) throws IOException {
        out.print(START_CHAR + msg + END_CHAR + END_TWO);
        out.flush();   
    } // end send
	
    public void close() {
    	out.close();
    	try {
    		socket.close();
    	} catch (IOException e) {
    		return;
    	}
    }
    
    public String confirm() {
    	String ret;
    	char last;
    	char next;
    	String flush;
    	
    	try { // read the MSH segment
    		last = (char) in.read();
    		//System.out.println("**" + last + "*");
    		ret = "";
    		next = (char) in.read();
    		ret = ret + next;
    		while (last != END_CHAR && next != END_TWO) {
    			last = next;
    			next = (char) in.read();
    			ret = ret + next;
    		}
    	} catch (IOException e) {
    		ret = "unable to read confirmation";
    		return ret;
    	}
    	ret = ret + "\n";
    	try { // read the MSA segment
    		last = (char) in.read();
    		//System.out.println("**" + last + "*");
    		ret = ret + last;
    		next = (char) in.read();
    		ret = ret + next;
    		while (last != END_CHAR && next != END_TWO) {
    			last = next;
    			next = (char) in.read();
    			ret = ret + next;
    		}
    	} catch (IOException e) {
    		ret = "unable to read confirmation";
    		return ret;
    	}
    	try {
    		flush = in.readLine();
    	} catch (IOException e) {
    		flush = "";
    	}
    	return ret;
    }
} // end class Receiver


