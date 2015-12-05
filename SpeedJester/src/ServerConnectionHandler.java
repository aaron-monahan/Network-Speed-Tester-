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
	    try {
	    	System.out.println( "size buffer: " + socket.getReceiveBufferSize());
	      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	      writer = new PrintWriter(socket.getOutputStream(), true);
	      String line;
	 
	      while(!done && ((line = reader.readLine()) != null)) {
	        if(line.compareTo("done") == 0)
	        {
        		done = true;
        		System.out.println( "Download Test concluded!");
	        }
	        writer.println(downloadChunk);
	      }
	      
	      
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
	
	
	
}
