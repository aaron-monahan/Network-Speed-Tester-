import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import javax.swing.*;

public class SpeedJesterMain extends JFrame implements ActionListener{
	
	private JButton client, server, exit;
	private JPanel controlButtonsPanel;
	private JLabel logo, footer;
	public static final int BUFFER_SIZE = 64; //KB
	public static final int TEST_DURATION = 20000;
	public SpeedJesterMain()
	{
		this.setSize(400,250);
		this.setTitle("SpeedJester");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getRootPane().setBorder( BorderFactory.createEmptyBorder(10,10,5,10));
		
		controlButtonsPanel = new JPanel();
		controlButtonsPanel.setLayout(new FlowLayout());
		controlButtonsPanel.setBorder( BorderFactory.createEmptyBorder(20,0,0,0));
		
		logo = new JLabel( new  ImageIcon("speedLogo.fw.png") );
		footer = new JLabel( "Developed by Wild Mutants", SwingConstants.CENTER);
		client = new JButton("Client Mode");
		server = new JButton("Server Mode");
		exit = new JButton("Exit");
		
		controlButtonsPanel.add(client);
		controlButtonsPanel.add(server);
		//controlButtonsPanel.add(exit);
		
		client.addActionListener(this);
		server.addActionListener(this);
		exit.addActionListener(this);
		
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		this.add(logo, "North");
		this.add(footer, "South");
		this.add(controlButtonsPanel, "Center");
		this.setVisible(true);
		
		repaint();
        validate();
//		Thread repainter = new Thread(new Runnable() {
//		    @Override
//		    public void run() {
//		        while (true) { // I recommend setting a condition for your panel being open/visible
//		            repaint();
//		            validate();
//		            try {
//		                Thread.sleep(1000);
//		            } catch (InterruptedException ignored) {
//		            }
//		        }
//		    }
//		});
//		repainter.setName("panel_repaint");
//		repainter.setPriority(Thread.MIN_PRIORITY);
//		repainter.start();
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
		this.setSize(300,200);
		Server s = new Server(5526);
		this.add(s);
		this.validate();
		this.repaint();
		try {
			s.run();
			s.closeConnection();
		} catch (IOException e) {
			System.out.println("Error: Connection could not be established...");
		}
	}
	
	private void clientMode()
	{
		this.remove(controlButtonsPanel);
		this.setSize(450,300);
		System.out.println("Client Mode!");
		Client c = new Client();
		this.add(c);

	}
	
	private void exitHandler()
	{
		int selection = JOptionPane.showConfirmDialog(null, "Are you sure you want to Exit?",
				"Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if( selection == JOptionPane.YES_OPTION)
			System.exit( 0 );
	}
}
