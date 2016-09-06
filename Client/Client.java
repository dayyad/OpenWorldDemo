
//TODO
//--Make client side movement animation to counter slow server connection.
//--Game still displays connected status when not connected properly (server is off)

//CONVERT ALL SERVER SYSTEMS TO DATAGRAM PACKETS

//Player joins server.
//Server tells client what chunck their player is in.
//This is added as a property of the player.
//if others players have the same chunck id, render those players

//Serverside, create a board of chuncks with ids.
//each player when created is given the spawn chunck as their id.
//if the player travels into an adjacent chucnk, their chucnk id gets changed to that one.
//the updateClient function makes sure to update all players as well as their current chucnks.

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Client {
	private JPanel p;
	private JFrame frame;
	private boolean connected;
	public double x;
	public double y;
	public int toX;
	public int toY;
	private String serverIp = "45.55.227.136";

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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(windowWidth, windowHeight);

		this.p = new Panel(this);

		//Sets the graphics so that we can draw things.
		JButton button = new JButton("Press enter to join localhost.");
		frame.add(p);

		//Adds a key listener to the panel
		p.requestFocus();
		p.repaint();
		p.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				System.out.println(e.getKeyChar());
				if(!connected){
					connected=true;
					status = "Connected.";
					advice = "WASD to move.";
					openConnection();
				} else {
					connection.send(Character.toString(e.getKeyChar()));
				}

				//If client has received move speed from the server, let the client draw moves instantly.
				if(moveSpeed!=0){
					if (Character.toString(e.getKeyChar()).equals("w")){
						player.setY(player.getY()-moveSpeed);
					} if (Character.toString(e.getKeyChar()).equals("a")){
						player.setX(player.getX()-moveSpeed);
					} if (Character.toString(e.getKeyChar()).equals("s")){
						player.setY(player.getY()+moveSpeed);
					} if (Character.toString(e.getKeyChar()).equals("d")){
						player.setX(player.getX()+moveSpeed);
					}
				}
				draw();
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

				System.out.println(e.getActionCommand());
			}
		});
	}

	//Opens connection to server.
	public void openConnection(){
		connection = new ClientConnection(this,serverIp);
	}

	public void draw(){
		p.repaint();
	}

	public static void main(String[] args) {
		new Client();
	}

}
