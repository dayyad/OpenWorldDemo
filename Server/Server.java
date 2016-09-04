import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Server {
	private ArrayList<ServerConnection> clients;
	private ServerSocket serverSocket;
	
	public int playerWidth = 30;
	public int playerHeight = 30;
	public int moveSpeed = 20;
	
	private JFrame frame;
	private JPanel panel;
	public World world;
	public ArrayList<Player> players;

	public Server(){
		//Init UI
		players=new ArrayList<Player>();
		clients = new ArrayList<ServerConnection>();
		frame = new JFrame("Server");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 300);
		panel = new JPanel();
		frame.add(panel);
		panel.requestFocus();
		
		//Listens for key pushes
		panel.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent e) {
				for(ServerConnection client : clients){
					client.send(Character.toString(e.getKeyChar()));
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
		
		//Attempt to start serversocket
		try {
			serverSocket = new ServerSocket(2222);
			while(true){
				clients.add(new ServerConnection(this,serverSocket.accept()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Search for a player by id, if not found return null
	public Player getPlayerById(int id){
		for(Player player : players){
			if(player.getId()==id)
				return player;
		}
		return null;
	}
	
	//Send all the clients current game state.
	public void updateClients(){
		for(ServerConnection client : clients){
			client.update();
		}
	}

	public static void main(String[] args) {
		new Server();
	}

}
