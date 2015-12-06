import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import javax.swing.*;


@SuppressWarnings("serial")
public class SpeedJesterMain extends JFrame implements ActionListener{
	
	private JButton client, server, exit;
	private JPanel controlButtonsPanel;
	public static final int BUFFER_SIZE = 64; //KB
	public static final int TEST_DURATION = 10000;
	public SpeedJesterMain()
	{
		this.setSize(500,500);
		this.setTitle("SpeedJester");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		controlButtonsPanel = new JPanel();
		controlButtonsPanel.setLayout(new FlowLayout());
		
		client = new JButton("Client Mode");
		server = new JButton("Server Mode");
		exit = new JButton("Exit");
		
		controlButtonsPanel.add(client);
		controlButtonsPanel.add(server);
		controlButtonsPanel.add(exit);
		
		client.addActionListener(this);
		server.addActionListener(this);
		exit.addActionListener(this);
		
		this.add(controlButtonsPanel, "Center");
		this.setVisible(true);
	}

	public static void main(String[] args)
	{
		SpeedJesterMain mainFrame = new SpeedJesterMain();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == client )
		{
			clientMode();
		}
		else if(e.getSource() == server)
		{
			serverMode();
		}
		else if(e.getSource() == exit )
		{
			exitHandler();
		}
		
	}
	
	private void serverMode()
	{
		this.remove(controlButtonsPanel);
		System.out.println("Server Mode!");
		
		Server s = new Server(5526);
		try {
			s.run();
			s.closeConnection();
		} catch (IOException e) {
			System.out.println("Error: Connection could not be established...");
		}
		this.repaint();
	}
	
	private void clientMode()
	{
		client.setEnabled(false);
		server.setEnabled(false);
		
		System.out.println("Client Mode!");
		
		Client c = new Client("127.0.0.1", 5526);
		try {
			c.downloadTest();
			//System.out.println(c.getDownloadSpeed() / 1048576);
			System.out.println(c.getDownloadSpeed() / 1024);
			c.closeConnection();
		} catch (IOException e) {
			System.out.println("Error: Connection could not be established...");
		}
		
		this.repaint();
	}
	
	private void exitHandler()
	{
		int selection = JOptionPane.showConfirmDialog(null, "Are you sure you want to Exit?",
				"Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if( selection == JOptionPane.YES_OPTION)
			System.exit( 0 );
	}
}
