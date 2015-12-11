import java.io.*;
import java.net.Socket;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Client extends JPanel implements ActionListener{
	private String host;
	private int port;
	private double downloadSpeed;
	private double uploadSpeed;
	private Socket socket;
	private BufferedReader dataIn;
	private PrintWriter dataOut;

    private Dimension size;
    private Insets insets;

    private JLabel down, up, ipL, portL;
    private JTextField ipB, portB;
    private JButton start, stop, connect;
    private String ip;

	public Client() {	
    	connectScreen();
	}

    public void connectScreen() {
        this.setLayout(null);
	    this.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.black) , "Server:") );
	    insets = this.getInsets();

		ipL = new JLabel("IP Address: ");
		portL = new JLabel("Port: ");
		
		ipB = new JTextField();
		portB = new JTextField();
		
		connect = new JButton("Connect");
		
		this.add(connect);
		this.add(ipL);
		this.add(portL);
		this.add(ipB);
		this.add(portB);
		
	    size = ipL.getPreferredSize();
	    ipL.setBounds(5 + insets.left, 5 + insets.top, size.width, size.height);
	    size = portL.getPreferredSize();
	    portL.setBounds(5 + insets.left, 30 + insets.top, size.width, size.height);
	    size = ipB.getPreferredSize();
	    ipB.setBounds(100 + insets.left, 5 + insets.top, 100 + size.width, size.height);
	    size = portB.getPreferredSize();
	    portB.setBounds(100 + insets.left, 30 + insets.top, 100 + size.width, size.height);
	    size = connect.getPreferredSize();
	    connect.setBounds(150 + insets.left, 60 + insets.top, 100 + size.width, size.height);
    }

    public void testScreen() {

		down = new JLabel("Download speed: ");
		up = new JLabel("Upload speed: ");
        
		start = new JButton("Start");
		stop = new JButton("Stop");
        
		this.add(start);
		this.add(stop);
		this.add(down);
		this.add(up);
		
	    size = start.getPreferredSize();
	    start.setBounds(50 + insets.left, 60 + insets.top, size.width, size.height);
	    size = stop.getPreferredSize();
	    stop.setBounds(150 + insets.left, 60 + insets.top, size.width, size.height);
	    size = up.getPreferredSize();
	    up.setBounds(5 + insets.left, 100 + insets.top, size.width, size.height);
	    size = down.getPreferredSize();
	    down.setBounds(5 + insets.left, 115 + insets.top, size.width, size.height);
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
		if( e.getSource() == start )
		{
            host = ipB.getText();
            port = Integer.parseInt(portB.getText());
		}
		
	}
}