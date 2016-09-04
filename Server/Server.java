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
	private int mapWidth = 5;
	private int mapHeight = 5;
	public int chunckWidth=1000;
	public int chunckHeight=700;
	
	private JFrame frame;
	private JPanel panel;
	public Chunck[][] chunckBoard;
	public Chunck chunck;
	public Chunck spawnChunck;
	public ArrayList<Player> players;

	public Server(){
		//Init UI
		players=new ArrayList<Player>();
		chunckBoard = new Chunck[mapWidth][mapHeight];
		this.fillBoard();
		spawnChunck=getChunckById(0);
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
	
	public Chunck getChunckById(int id){
		for(int x =0;x<chunckBoard.length;x++){
			for(int y=0;y<chunckBoard[0].length;y++){
				if(chunckBoard[x][y].getId()==id){
					return chunckBoard[x][y];
				}
			}	
		}
		return null;
	}
	
	//Geneerates chuncks for each slot on the board
	public void fillBoard(){
		for (int x= 0;x<chunckBoard.length;x++){
			for(int y=0;y<chunckBoard[0].length;y++){
				chunckBoard[x][y]=new Chunck(x,y);
			}
		}
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
