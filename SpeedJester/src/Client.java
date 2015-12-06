import java.io.*;
import java.net.Socket;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Client extends JFrame implements ActionListener{
	private String host;
	private int port;
	private double downloadSpeed;
	private double uploadSpeed;
	private Socket socket;
	private BufferedReader dataIn;
	private PrintWriter dataOut;

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void downloadTest(){
		long startTestTime = 0;
		long currentTime = 0;
		int interactions = 0;

		try {
			openConnection();
			
			System.out.println("Size buffer: " + socket.getReceiveBufferSize());
			
			downloadSpeed = 0.0;
			startTestTime = System.currentTimeMillis();
			
			dataOut.println("startdownload");
			
			while ((startTestTime + SpeedJesterMain.TEST_DURATION) > currentTime) {
				interactions++;
				currentTime = System.currentTimeMillis();
				dataOut.println("echo");
				dataIn.readLine();
				setDownloadSpeed((double) (currentTime - startTestTime), interactions);
			}
			
			dataOut.println("downloaddone");
			
			closeConnection();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void uploadTest(){
		String uploadChunk = (new ChunkGenerator(SpeedJesterMain.BUFFER_SIZE)).generate();
		String line;
		boolean done = false;
		
		try {
			openConnection();
			
			System.out.println("size buffer: " + socket.getReceiveBufferSize());
			
			dataOut.println("startupload");

			while (!done && ((line = dataIn.readLine()) != null)) {
				if (line.compareTo("uploaddone") == 0) {
					done = true;
					System.out.println("Upload Test concluded!");
				}
				System.out.println(line);
				dataOut.println(uploadChunk);
			}
			
			closeConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	private void setDownloadSpeed(double time, int interactions) {
		// buffer 16384 = 16KB
		downloadSpeed += ((interactions * SpeedJesterMain.BUFFER_SIZE) / (time / 1000)) * 8;
		System.out.println("Interaction : " + interactions + "Time : " + time +
							"Download : " + (downloadSpeed / interactions) / 1024);

	}

	public double getDownloadSpeed() {
		return downloadSpeed;
	}
	
	public void openConnection() throws IOException {
		socket = new Socket(host, port);
		socket.setSoTimeout(20000); // times out if no data came after 10 seconds
		dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		dataOut = new PrintWriter(socket.getOutputStream(), true);
	}

	public void closeConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Error: Socket already closed...");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
