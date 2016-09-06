import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

//server adds connection to new connection list if not already present
//gives new connection the recieved inet address
//connection the listens



public class Server {
	private ArrayList<ServerConnection> clients;
	private DatagramSocket serverSocket;

	public int playerWidth = 30;
	public int playerHeight = 30;
	public int moveSpeed = 20;
	private int mapWidth = 5;
	private int mapHeight = 5;
	public int chunckWidth=450;
	public int chunckHeight=450;

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

		//TOGLE FOR UI ---------------------------------

//		frame = new JFrame("Server");
//		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(300, 300);
//		panel = new JPanel();
//		frame.add(panel);
//		panel.requestFocus();

		//Listens for key pushes

//		panel.addKeyListener(new KeyListener(){
//			@Override
//			public void keyPressed(KeyEvent e) {
//				for(ServerConnection client : clients){
//					client.send(Character.toString(e.getKeyChar()));
//				}
//			}
//			@Override
//			public void keyReleased(KeyEvent e) {
//			}
//			@Override
//			public void keyTyped(KeyEvent e) {
//			}
//		});

		//TOGLE FOR UI ---------------------------------

		//Attempt to start serversocket

		try {
			serverSocket = new DatagramSocket(2222);
			byte[] receiveData = new byte[1];
			System.out.println("Server started...");
			while(true){
				System.out.println("Looped main server receive loop once.");
				DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
				serverSocket.receive(receivePacket);
				InetSocketAddress remoteSocketAddress = new InetSocketAddress(receivePacket.getAddress(),receivePacket.getPort());

				boolean found = false;
				for(ServerConnection client : clients){
					if(client.socketAddress.equals(remoteSocketAddress)){
						client.processLine(receivePacket.getData().toString());
						found=true;
					}
				}
				if(!found){
					clients.add(new ServerConnection(this,remoteSocketAddress));
				}
				System.out.println("Server recieved a bit of data :)");

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String string, SocketAddress address){
		try {
			byte[] sendData = new byte[128];
			sendData = string.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,address);
			serverSocket.send(sendPacket);
		} catch (IOException e) {
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
