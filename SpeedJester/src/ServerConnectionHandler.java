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
	    try {
	    	System.out.println( "size buffer: " + socket.getReceiveBufferSize());
	      reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	      writer = new PrintWriter(socket.getOutputStream(), true);
	 
	      // The read loop. Code only exits this loop if connection is lost / client disconnects
	      while(true) {
	        String line = reader.readLine();
	        if(line == "done") break;
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
