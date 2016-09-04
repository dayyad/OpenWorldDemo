import java.awt.Graphics;

//TODO 
//--Make the chucnks have coordinates and not just ids.
//--make the client panel display the coordinates of the player in the world.
//--Game still displays connected status when not connected properly (server is off)

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
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Client {
	private Graphics g;
	private JPanel p;
	private JFrame frame;
	private boolean connected;
	public double x;
	public double y;
	public int toX;
	public int toY;
	
	private int windowWidth = 450;
	private int windowHeight =450;
	public int playerWidth = 30;
	public int playerHeight = 30;
	
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

		Socket socket;
		try {
			socket = new Socket("localhost",2222);
			connection = new ClientConnection(this,socket);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(){
		p.repaint();
	}

	public static void main(String[] args) {
		new Client();
	}

}
