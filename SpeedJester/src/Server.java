import java.io.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends JFrame implements ActionListener{
	private int port;
	private boolean alive = false;
	private JButton start, stop, exit;
	private ServerSocket serverSocket;
	private ExecutorService executorService;
	
    public Server(int port){
    	super();
    	this.port = port;
    	
    	start = new JButton("Start");
    	stop = new JButton("Stop");
		exit = new JButton("Exit");
		
		this.add(start);
		this.add(stop);
		this.add(exit);
		
		start.addActionListener(this);
		stop.addActionListener(this);
		exit.addActionListener(this);
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
