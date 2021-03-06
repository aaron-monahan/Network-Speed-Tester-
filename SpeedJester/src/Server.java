import java.io.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.InetAddress;

public class Server extends JPanel implements ActionListener{
	private int port;
	private boolean alive = true;
	private JButton start, stop, exit;
	private ServerSocket serverSocket;
	private ExecutorService executorService;
	
    public Server(int port){
    	this.port = port;
        String ipAdd = "Null";
        
        this.setLayout ( new BorderLayout() );
        this.setBorder( BorderFactory.createEmptyBorder(10,10,10,10));
        JLabel servLab = new JLabel("Server listening...", SwingConstants.CENTER);
        try {
            ipAdd = InetAddress.getLocalHost().toString();
        }
        catch (UnknownHostException e) {
            ipAdd = "Ip: Unknown";
        }
        JLabel ipLab = new JLabel( "Ip: " + ipAdd , SwingConstants.CENTER);
        JLabel portLab = new JLabel( "Port: " + port , SwingConstants.CENTER);
    	
    	this.add(servLab, "North");
    	this.add(ipLab, "Center");
    	this.add(portLab, "South");
    	
    	//this.repaint();
    	
    	//start = new JButton("Start");
    	//stop = new JButton("Stop");
		//exit = new JButton("Exit");
		
		//this.add(start);
		//this.add(stop);
		//this.add(exit);
		
		//InetAddress.getLocalHost()
		
		//start.addActionListener(this);
		//stop.addActionListener(this);
		//exit.addActionListener(this);
    }
    
    public void run() throws IOException{
        System.out.println("Starting service listening on " + port);
        executorService = Executors.newFixedThreadPool(10); //only accept 10 connections per server
        serverSocket = new ServerSocket(this.port);
        
        while(alive)
        {
			Socket socket = serverSocket.accept();
//			socket.setSoTimeout(10000); // inputstream's read times out if no data came after 10 seconds
			executorService.execute(new ServerConnectionHandler(socket));
			System.out.println("Socket successfully opened from client, beginning file transfer.");
        }
    }
    
    public void closeConnection(){
    	try {
    		executorService.shutdown();
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("Error: Socket already closed...");
		}
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == start )
		{
			alive = true;
			try {
				run();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			stop.setEnabled(true);
			start.setEnabled(false);
		}
		else if(e.getSource() == stop)
		{
			alive = false;
			start.setEnabled(true);
			stop.setEnabled(false);
		}
		else if(e.getSource() == exit )
		{
			closeConnection();
		}
	}

}
