import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Scanner;


public class ServerConnection {
	private Server server;
	private DatagramSocket socket;
	private Scanner scanner;
	private PrintWriter writer;
	public InetAddress remoteAddress;
	public final int connectionId;
	private static int lastId=0;

	public ServerConnection(Server server,InetAddress remoteAddress){
		this.server=server;
		this.connectionId=lastId++;

		initPlayer();
	}

	public void send(String string){
		try {
			byte[] sendData = new byte[128];
			sendData = string.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,remoteAddress,2222);
			socket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update(){
		//Sends player their chunck coordinates.
		send("setChunckCoordinates" + " " + server.getChunckById(server.getPlayerById(connectionId).getChunckId()).getxPos() + " " + server.getChunckById(server.getPlayerById(connectionId).getChunckId()).getyPos());
		for(Player player : server.players){
			//Sends information about all the players
			String playerPacket = "setPlayer" + " " + player.getX() + " " + player.getY()
			+ " " + player.getWidth() + " " + player.getHeight() + " " + player.getId() + " " + player.getChunckId();
			send(playerPacket);
		}
	}

	//For setting up a player that has just connected.
	public void initPlayer(){
		send("setConnectionId " + connectionId);
		send("setMoveSpeed " + server.moveSpeed);
		server.players.add(new Player(60,60,server.playerWidth,server.playerHeight,connectionId,server.spawnChunck.getId()));
		server.updateClients();
	}

	//ServerListener

	public void processLine(String line){
		Player player = server.getPlayerById(connectionId);
		if(player!=null){
			if(line.equals("w")){
				player.setY(player.getY()-server.moveSpeed);
			}
			if(line.equals("a")){
				player.setX(player.getX()-server.moveSpeed);
			}
			if(line.equals("s")){
				player.setY(player.getY()+server.moveSpeed);
			}
			if(line.equals("d")){
				player.setX(player.getX()+server.moveSpeed);
			}
			checkTravel();
		}

		server.updateClients();
		System.out.println("Server recieved: " +line);
	}

	//Checks if the player has adventured beyond their respective chucnk, and therefore if the player needs to be moved
	//to a different chunck.

	private void checkTravel(){
		Player player = server.getPlayerById(connectionId);
		int x = player.getX();
		int y = player.getY();
		int chunckX = server.getChunckById(player.getChunckId()).getxPos();
		int chunckY = server.getChunckById(player.getChunckId()).getyPos();

		if (x>server.chunckWidth && chunckX<server.chunckBoard.length-1){
			//To right of chunck
				System.out.println("Ran into wall right");
				player.setChunckId(server.chunckBoard[chunckX+1][chunckY].getId());
				player.setX(0+server.playerWidth);
		} else if(x>server.chunckWidth){
			System.out.println("moved player left");
			player.setX(server.chunckWidth-server.playerWidth);
		} if(x<0 && chunckX>0){
			//To left of chunck
			    System.out.println("Ran into wall left");
				player.setChunckId(server.chunckBoard[chunckX-1][chunckY].getId());
				player.setX(server.chunckWidth-server.playerWidth);
		} else if(x<0){
			System.out.println("moved player right");
			player.setX(0+server.playerHeight);
		} if(y>server.chunckHeight && chunckY < server.chunckBoard[0].length-1){
			//Bellow chunck
				player.setChunckId(server.chunckBoard[chunckX][chunckY+1].getId());
				player.setY(0+server.playerHeight);
				System.out.println("Ran into wall down");
		} else if(y>server.chunckHeight){
			System.out.println("moved player up");
			player.setY(server.chunckHeight-server.playerHeight);
		} if(y<0 && chunckY>0){
			//Above chunck
				player.setChunckId(server.chunckBoard[chunckX][chunckY-1].getId());
				player.setY(server.chunckHeight-server.playerHeight);
				System.out.println("Ran into wall up");
		} else if(y<0){
			System.out.println("moved player down");
			player.setY(0+server.playerHeight);
		}

		server.updateClients();
	}

}
