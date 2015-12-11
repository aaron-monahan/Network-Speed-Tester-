import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import javax.swing.*;


@SuppressWarnings("serial")
public class SpeedJesterMain extends JFrame implements ActionListener{
	
	private JButton client, server, exit;
	private JPanel controlButtonsPanel;
	private ClientPanel clientPanel;
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
		    this.remove(controlButtonsPanel);
		    Insets insets = this.getInsets();
		    this.setSize(400 + insets.left + insets.right, 300 + insets.top + insets.bottom);
		    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		    clientPanel = new ClientPanel();
		    this.add(clientPanel);
		    this.repaint();
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
//		Client c = new Client("192.168.1.4", 5526);
		Client c = new Client( clientPanel.getIp(), clientPanel.getPort() );
		
		c.downloadTest();
		//System.out.println(c.getDownloadSpeed() / 1024);
		c.uploadTest();
		
		
		
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
