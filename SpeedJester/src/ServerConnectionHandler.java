import java.io.*;
import java.net.Socket;

public class ServerConnectionHandler implements Runnable{
	
	private Socket socket;
	private String downloadChunk;
	
	public ServerConnectionHandler(Socket socket){
		this.socket = socket;
		downloadChunk = (new ChunkGenerator(SpeedJesterMain.BUFFER_SIZE)).generate();
	}

	@Override
	public void run() {
		BufferedReader reader = null;
	    PrintWriter writer = null;
	    boolean done = false;

		long startTestTime = 0;
	    	long currentTime = 0;
	    	int interactions = 0;

	    try {
	    	System.out.println( "size buffer: " + socket.getReceiveBufferSize());
	      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	      writer = new PrintWriter(socket.getOutputStream(), true);
	      String line;
	 
	      while(!done && ((line = reader.readLine()) != null)) {
	        if(line.compareTo("download done") == 0)
	        {
        		done = true;
        		System.out.println( "Download Test concluded!");
	        }
	        writer.println(downloadChunk);
	      }

		downloadSpeed = 0;
	    	startTestTime =  System.currentTimeMillis( );
	    	dataOut.println("start upload");
		while((startTestTime + SpeedJesterMain.TEST_DURATION) > currentTime)
		{
			
			dataIn.readLine();
			interactions++;
			currentTime = System.currentTimeMillis();
			dataOut.println("upload " + setDownloadSpeed((double) (currentTime - startTestTime),interactions));
		}
	        dataOut.println("upload done");


	      
	    } catch (IOException e) {
	      throw new RuntimeException(e);
	    } finally {
	      try {
	        if(reader != null) reader.close();
	        if(writer != null) writer.close();
	      } catch (IOException e) {
	        throw new RuntimeException(e);
	      }
	    }

	private long setDownloadSpeed(double time, int interactions) {
    	// buffer 16384 = 16KB 

    		System.out.println( "interaction : " + interactions);
	    	System.out.println( "time : " + time);
	    	System.out.println( "download : " + (downloadSpeed/1024));
		
		return ((interactions * SpeedJesterMain.BUFFER_SIZE) / (time / 1000)) * 8;
    	
    	}
	}
	
	
	
}
