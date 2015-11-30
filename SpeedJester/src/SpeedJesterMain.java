import java.io.IOException;

public class SpeedJesterMain {
	
	public static void main(String[] args)
	{
		
		if(Integer.parseInt(args[0]) == 1)
		{
			System.out.println("Server Mode!");
			
			Server s = new Server(5526);
			try {
				s.run();
				s.closeConnection();
			} catch (IOException e) {
				System.out.println("Error: Connection could not be established...");
			}
		}else{
			System.out.println("Client Mode!");
			
			Client c = new Client("127.0.0.1", 5526);
			try {
				c.downloadTest();
				c.closeConnection();
			} catch (IOException e) {
				System.out.println("Error: Connection could not be established...");
			}
		}
		
		
	}
}
