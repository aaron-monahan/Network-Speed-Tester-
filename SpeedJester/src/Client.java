import java.io.*;
import java.net.Socket;

public class Client {
	private String host;
	private int port;
	private double downloadSpeed;
	private Socket socket;
    
    public Client(String host, int port){
    	this.host = host;
    	this.port = port;
    }
    
    
    public void downloadTest() throws IOException
    {
    	long startTestTime = 0;
    	long testDuration = 20000; //20 seconds
    	long currentTime = 0;
    	int interactions = 0;
    	socket =  new Socket(host, port);
    	socket.setSoTimeout(10000); // times out if no data came after 10 seconds
    	
//    	boolean exitLoop = true;
    	BufferedReader dataIn =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	PrintWriter dataOut = new PrintWriter(socket.getOutputStream(), true);
    	System.out.println( "size buffer: " + socket.getReceiveBufferSize());
        
    	downloadSpeed = 0;
    	startTestTime =  System.currentTimeMillis( );
    	dataOut.println("start");
        while((startTestTime + testDuration) > currentTime)
        {
        	dataOut.println("d");
        	dataIn.readLine();
        	interactions++;
        	currentTime = System.currentTimeMillis();
        	setDownloadSpeed((double) (currentTime - startTestTime),interactions);
        }
        dataOut.println("done");
    }
    
    private void setDownloadSpeed(double time, int interactions)
    {
    	// buffer 16384 = 16KB 
    	downloadSpeed = ((interactions * SpeedJesterMain.BUFFER_SIZE) / (time / 1000)) * 8;
    	
    	
    		System.out.println( "interaction : " + interactions);
	    	System.out.println( "time : " + time);
	    	System.out.println( "download : " + (downloadSpeed/1024));
    	
    }

    public double getDownloadSpeed(){
    	return downloadSpeed;
    }
    
    public void closeConnection(){
    	try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Error: Socket already closed...");
		}
    }
	
}
