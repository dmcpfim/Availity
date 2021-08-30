package availity.test;

import java.net.InetAddress;
import java.time.Instant;


public class Main {
	private static String nameList[] = {"DOE,JOHN","SMITH,RALPH","JOHNSON,MARY","TEST,TEST","WILSON,FRED","THOMPSON,JULIE"};
	private static String idList[] = {"M374737","S583838","P483858","Z123456","M578472","P858382"};
	
	// Messages are generated for one of 6 patients, 1 is a test patients, others are regular
	// 10% of messages are addressed to Component3, rest to Component2
	// message is just a simple MSH element
	private static String getMessage() {
		String msg;
		String dest;
		Instant timestamp = Instant.now();
		int userNum = (int) Math.floor(Math.random() * 6);
		
		if (Math.random() < 0.1) {
			dest = "Component3";
		} else {
			dest = "Component2";
		}
		msg = "MSH|^~\\&|TEST|Component1|" + dest + "|";
		msg = msg + timestamp.toString(); 
		msg = msg + "|" + nameList[userNum] + "|" + idList[userNum] + "|ADT^A01^ADT_A01|" + userNum + "|";
		return msg;		
	} //end getMessage
	
	
    public static void main(String[] args) throws Exception {
    	Receiver client = new Receiver(InetAddress.getByName("localhost"), 6661);
        String msg;
    	
        System.out.println("\n\nConnected to Server:\n\n");
        
        // send message every 5 seconds
        for (int i = 0; i < 20; i++) {
        	msg = getMessage();
        	client.send(msg);
        	Thread.sleep(5000);
        	msg = client.confirm();
        	System.out.println(msg);
        }
        
        client.close();
        System.out.println("connection closed");
    } // end main function

	
} // end class Main
