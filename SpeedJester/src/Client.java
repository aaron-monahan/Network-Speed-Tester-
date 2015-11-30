import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private String host;
	private int port;
	private Socket socket;
	
    private static final int BUFFER_SIZE = 8192;
    private static final String INVALID_FILENAME = "The filename does not exist: ";
    
    
    public Client(String host, int port){
    	this.host = host;
    	this.port = port;

    }
    
    
    public void downloadTest() throws IOException
    {
    	socket =  new Socket(host, port);
    	int i = 0;
//    	boolean exitLoop = true;
    	BufferedReader dataIn =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	PrintWriter dataOut = new PrintWriter(socket.getOutputStream(), true);
       
        
        while(true)
        {
        	dataOut.println(" " + ++i + " ");
        	System.out.println(dataIn.readLine());
        	if (i == 100) break;
        }
        
        
    }
    
    public void closeConnection(){
    	try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Error: Socket already closed...");
		}
    }
	
}
