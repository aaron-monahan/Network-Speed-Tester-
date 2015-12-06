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
	    startTestTime =  System.currentTimeMillis( );
	    writer.println("start upload");
		while((startTestTime + SpeedJesterMain.TEST_DURATION) > currentTime)
		{
			
			reader.readLine();
			interactions++;
			currentTime = System.currentTimeMillis();
			writer.println("upload " + setDownloadSpeed((double) (currentTime - startTestTime),interactions));
		}
		writer.println("upload done");


	      
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


	}
	
	private double setDownloadSpeed(double time, int interactions) {
    	// buffer 16384 = 16KB 
			
		double downloadSpeed = ((interactions * SpeedJesterMain.BUFFER_SIZE) / (time / 1000)) * 8;
		
    		System.out.println( "interaction : " + interactions);
	    	System.out.println( "time : " + time);
	    	System.out.println( "download : " + (downloadSpeed/1024));
		
		return downloadSpeed;
    	
    }
	
}
