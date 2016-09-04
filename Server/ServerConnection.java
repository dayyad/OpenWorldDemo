import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerConnection {
	private Server server;
	private Socket socket;
	private Listener listener;
	private Scanner scanner;
	private PrintWriter writer;
	public final int connectionId;
	private static int lastId=0;

	public ServerConnection(Server server,Socket socket){
		this.server=server;
		this.socket=socket;
		this.connectionId=lastId++;

		listener=new Listener();
		listener.start();
		
		initPlayer();
	}

	public void send(String string){
		try {
			writer=new PrintWriter(socket.getOutputStream());
			writer.println(string + "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update(){
		for(Player player : server.players){
			String playerPacket = "setPlayer" + " " + player.getX() + " " + player.getY()
			+ " " + player.getWidth() + " " + player.getHeight() + " " + player.getId() + " " + player.getChunckId();
			send(playerPacket);
		}
	}
	
	//For setting up a player that has just connected.
	public void initPlayer(){
		send("setConnectionId " + connectionId);
		server.players.add(new Player(0,0,server.playerWidth,server.playerHeight,connectionId,server.spawnChunck.getId()));
		server.updateClients();
	}

	//ServerListener
	class Listener extends Thread{

		public void run(){
			//Initialise the scanner listening
			try{
				scanner = new Scanner (socket.getInputStream());
				while(socket!=null){
					if(scanner.hasNextLine()){
						String line = scanner.nextLine();
						processLine(line);
					}
				}

			} catch(IOException e){
			}
		}
	}

	private void processLine(String line){
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
		
	}

}
