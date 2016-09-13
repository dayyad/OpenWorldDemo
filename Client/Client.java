
//TODO
//--Make client side movement animation to counter slow server connection.
//--Implement better server client handshake when connecting to ensure conenction
//--Deal with disconnections properly.

//CONVERT ALL SERVER SYSTEMS TO DATAGRAM PACKETS

//Player joins server.
//Server tells client what chunck their player is in.
//This is added as a property of the player.
//if others players have the same chunck id, render those players

//Serverside, create a board of chuncks with ids.
//each player when created is given the spawn chunck as their id.
//if the player travels into an adjacent chucnk, their chucnk id gets changed to that one.
//the updateClient function makes sure to update all players as well as their current chucnks.

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client {
	private JPanel p;
	private JFrame frame;
	private JTextArea messageField;
	public JTextArea console;
	private boolean connected;
	public double x;
	public double y;
	public int toX;
	public int toY;
	private String serverIp = "128.199.236.107";

	private int windowWidth = 1000;
	private int windowHeight =700;
	public int playerWidth = 30;
	public int playerHeight = 30;

	//Imported from server.
	public int moveSpeed = 0;
	public int[] chunckCoordinates= new int[2];
	public String status;
	public String advice;
	public Player player;
	public ArrayList<Player> players;
	public World world;
	public ClientConnection connection;

	public Client(){
		x=0;
		y=0;
		toX=10;
		toY=10;
		connected = false;
		players = new ArrayList<Player>();
		status = "Not connected.";
		advice = "Press any key to connect to server.";

		//Creates the jframe/
		frame = new JFrame("Client");
		frame.setVisible(true);
		frame.setAutoRequestFocus(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(windowWidth, windowHeight);

		this.p = new Panel(this);
		//Draws console
//
//		console=new JTextArea("Console:");
//		frame.add(console);
//		console.setBounds(900, 0, 100, frame.getHeight());
//		console.setBackground(new Color(240,240,240));

		//Sets the graphics so that we can draw things.
		JButton button = new JButton("Press enter to join localhost.");
		frame.add(p);

		//Adds a key listener to the panel
		p.requestFocus();
		p.repaint();
		p.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyChar());
				if(connection==null){
					status = "Connecting...";
					advice = "Try browsing memes while you wait...";
					openConnection();
				} else {
					connection.send(Character.toString(e.getKeyChar()));
				}

				String key = Character.toString(e.getKeyChar());

				if(key.equals("m")){
					doDraftMessage();
				}

				if(KeyEvent.getKeyText(e.getKeyCode()).equals("Enter")){
					doSendMessage();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				//System.out.println(e.getActionCommand());
			}
		});

		//Adds listener for when game is closed to send exit message to server
		//and to stop execution of client.
		frame.addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		    	exitGame();
		    }
		});
	}

	//Exit protocol.
	public void exitGame(){
		if(connection!=null){
    		connection.send("exit");
        	connection.close();
    	}
    	frame.dispose();
    	System.exit(0);
	}

	//Opens connection to server.
	public void openConnection(){
		connection = new ClientConnection(this,serverIp);
	}
	long endTime;
	long startTime = System.nanoTime();

	public void draw(){
		endTime = System.nanoTime();
		//System.out.println(endTime-startTime);
			p.repaint();
			startTime = System.nanoTime();
	}

	public void doDraftMessage(){
		if(messageField==null){
			messageField = new JTextArea();
			frame.add(messageField);
			messageField.setText("Enter Message:");
			System.out.println("Tryng to open text box");
			messageField.setBounds(frame.getWidth()/2-100, frame.getHeight()/2 -100, 200, 100);
			messageField.setBackground(new Color(240,240,240));
			messageField.addKeyListener(new KeyListener(){

				@Override
				public void keyTyped(KeyEvent e) {

				}

				@Override
				public void keyPressed(KeyEvent e) {
					if(KeyEvent.getKeyText(e.getKeyCode()).equals("Enter")){
						doSendMessage();
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {

				}

			});
			messageField.setVisible(true);
		} else if(!messageField.isVisible()){
			messageField.setVisible(true);
			messageField.setText("Enter Message: ");
		}
	}

	public void doSendMessage(){
		if(messageField!=null && messageField.isVisible()){

			// UNTAB THIS FOR THE GAME TO WORK connection.send("message " + messageField.getText());
			messageField.setVisible(false);
			messageField.setFocusable(false);
			frame.requestFocus();
			System.out.println("THIS WORKS");
		}
	}

	public static void main(String[] args) {
		new Client();
	}

}
