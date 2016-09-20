import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
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
	public boolean uniDebugMode =true;

	public int playerWidth = 30;
	public int playerHeight = 30;
	public int moveSpeed = 20;
	private int mapWidth = 5;
	private int mapHeight = 5;
	public int chunckWidth=800;
	public int chunckHeight=800;

	private JFrame frame;
	private JPanel panel;
	public Chunck[][] chunckBoard;
	public Chunck chunck;
	public Chunck spawnChunck;
	public ArrayList<Player> players;
	private String serverIp = "130.195.6.35";

	public Server(){
		//Init UI
		players=new ArrayList<Player>();
		chunckBoard = new Chunck[mapWidth][mapHeight];
		this.fillBoard();
		spawnChunck=getChunckById(0);
		clients = new ArrayList<ServerConnection>();

		//TOGLE FOR UI ---------------------------------

	/*frame = new JFrame("Server");
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
	});*/

		//TOGLE FOR UI ---------------------------------

		//Attempt to start serversocket

		try {
			serverSocket = new DatagramSocket(new InetSocketAddress(InetAddress.getByName(serverIp), 3322));
			System.out.println("Server started...");
			while(true){
				byte[] receiveData = new byte[1000];
				//System.out.println("Looped main server receive loop once.");
				DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
				serverSocket.receive(receivePacket);
				//System.out.println("Packet recieved from: " + receivePacket.getSocketAddress().toString());
				SocketAddress remoteSocketAddress = receivePacket.getSocketAddress();
				//System.out.println("MORE INFO: " + Integer.toString(receivePacket.getPort()) + " " + receivePacket.getAddress().toString());

				boolean found = false;
				for(ServerConnection client : clients){
					//If client not connecting from existing address then add new client.
					if(uniDebugMode){
						//Assumes server running on same machine at uni behind firewall.
						if(client.remoteAddress.equals(client.remoteAddress.equals(serverIp) && client.remotePort == receivePacket.getPort())){
							client.processLine(new String(receivePacket.getData()));
							found=true;
						}
					} else {
						if(client.remoteAddress.equals(receivePacket.getAddress()) && client.remotePort == receivePacket.getPort()){
							client.processLine(new String(receivePacket.getData()));
							found=true;
						}
					}


				}

				//Special add clinet case if at uni.
				if(uniDebugMode && !found){
						clients.add(new ServerConnection(this,InetAddress.getLocalHost(),receivePacket.getPort()));
				} else {
					if(!found){
						clients.add(new ServerConnection(this,receivePacket.getAddress(),receivePacket.getPort()));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void debug(String string){
		System.out.println(string);
	}

	//Sends string to specified client
	public void send(String string, ServerConnection connection){
		debug("Trying to send: " + string);
		try {
			byte[] sendData = string.getBytes();

			DatagramPacket sendPacket;

			//If in debug mode always send to self.
			if(uniDebugMode){
				sendPacket = new DatagramPacket(sendData, sendData.length, new InetSocketAddress(serverIp, connection.remotePort));
			}else{
				sendPacket = new DatagramPacket(sendData,sendData.length,connection.remoteAddress,connection.remotePort);
			}
			serverSocket.send(sendPacket);
			debug("Sent : " + string + " to: " + connection.remoteAddress.getHostName() + ":" + connection.remotePort );

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Sends string to all connected clients
	public void sendAll(String string){
		for(ServerConnection client : clients){
			send(string,client);
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

	public void removeClient(int connectionId){
		for(ServerConnection client : clients){
			if(client.connectionId==connectionId){
				client=null;
				//System.out.println("Client disconnected.");
			}
		}
	}

	public static void main(String[] args) {
		new Server();
	}

}
