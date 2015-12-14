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

    private JLabel down, up, ipL, portL;
    private JTextField ipB, portB;
    private JButton change, stop, connect;
    private JPanel ipPortArea, ConBut;

	public Client() {
        this.setLayout(new FlowLayout());
    	connectScreen();
	}

    public void connectScreen() {
        ipPortArea = new JPanel (new FlowLayout());
        ConBut = new JPanel (new FlowLayout());
        ConBut.setBorder( BorderFactory.createEmptyBorder(20,0,0,0));
	    ipPortArea.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.black) , "Server:") );

		ipL = new JLabel("IP Address: ");
		portL = new JLabel("Port: ");
		
		ipB = new JTextField();
		portB = new JTextField();
		ipB.setPreferredSize (new Dimension(100,24));
		portB.setPreferredSize (new Dimension(100,24));
		
		connect = new JButton("Connect");
		ConBut.add(connect);
		connect.addActionListener(this);
		
		ipPortArea.add(ipL);
		ipPortArea.add(ipB);
		ipPortArea.add(portL);
		ipPortArea.add(portB);
		
		this.add(ipPortArea);
		this.add(ConBut);
    }

    public void testScreen() {
        this.remove(ipPortArea);
        this.remove(ConBut);
        
        JPanel downPanel = new JPanel ();
        downPanel.setLayout(new BoxLayout(downPanel, BoxLayout.Y_AXIS));
        JPanel upPanel = new JPanel ();
        upPanel.setLayout(new BoxLayout(upPanel, BoxLayout.PAGE_AXIS));
        downPanel.setBorder( BorderFactory.createEmptyBorder(20,0,0,0));
        upPanel.setBorder( BorderFactory.createEmptyBorder(20,0,0,0));
        
		down = new JLabel("Download", SwingConstants.CENTER);
		up = new JLabel("Upload", SwingConstants.CENTER);
        
		change = new JButton("Change Server");
		stop = new JButton("Stop");
		
		JPanel downSpeed = new JPanel ();
		JPanel upSpeed = new JPanel ();
        downSpeed.setBackground(Color.GREEN);
        upSpeed.setBackground(Color.GREEN);
        
        downSpeed.setPreferredSize (new Dimension(70,70));
        upSpeed.setPreferredSize (new Dimension(70,70));
        
        JLabel downSpeedL = new JLabel ("0.0 Mbps", SwingConstants.CENTER);
        JLabel upSpeedL = new JLabel ("0.0 Mbps", SwingConstants.CENTER);
        
        downSpeed.add(downSpeedL, "Center");
        upSpeed.add(upSpeedL, "Center");
        
        downPanel.add(down);
        downPanel.add(downSpeed);
        downPanel.add(stop);
        
        upPanel.add(up);
        upPanel.add(upSpeed);
        upPanel.add(change);
        
		this.add(downPanel);
		this.add(upPanel);
		this.repaint();
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
		if( e.getSource() == connect )
		{
            host = ipB.getText();
            port = Integer.parseInt(portB.getText());
            testScreen();
            downloadTest();
            uploadTest();
		}
		
	}
}
