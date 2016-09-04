import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnection {
	private Client client;
	private Socket socket;
	private Listener listener;
	private Scanner scanner;
	private PrintWriter writer;
	public int connectionId;

	public ClientConnection(Client client,Socket socket){
		this.client=client;
		this.socket=socket;
		this.connectionId=0;

		listener=new Listener();
		listener.start();
	}

	public void send(String string){
		try {
			writer=new PrintWriter(socket.getOutputStream());
			writer.println(string + "\n");
			writer.flush();
			System.out.println("Server sent to user: " + string);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		System.out.println(line);
		Scanner lineScanner = new Scanner(line);
		while (lineScanner.hasNext()){
			String nextPack = lineScanner.next();
			
			//Scans for connection id, setting current connection id to this.
			if(nextPack.equals("setConnectionId")){
				if(lineScanner.hasNextInt()){
					this.connectionId=lineScanner.nextInt();
					System.out.println("Client: Successfuly set connectionId to: " + connectionId);
				}
			}
			//Scans player packets and updates the local player data.
			if(nextPack.equals("setPlayer")){
				int x = lineScanner.nextInt();
				int y = lineScanner.nextInt();
				int width = lineScanner.nextInt();
				int height = lineScanner.nextInt();
				int id = lineScanner.nextInt();
				int chunckId = lineScanner.nextInt();
				
				boolean found = false;
				for(Player player : client.players){
					if(player.getId()==id){
						player.setX(x);
						player.setY(y);
						player.setWidth(width);
						player.setHeight(height);
						player.setChunckId(chunckId);
						found=true;
						System.out.println("Client: found player, dont have to add");
					}
				}
				if(!found){
					Player newPlayer=new Player(x,y,width,height,id,chunckId);
					client.players.add(newPlayer);
					System.out.println("Client: added new player.");
					//If the newly created player has same id as the connection then set the player to be our player.
					if(newPlayer.getId()==connectionId)
						client.player=newPlayer;
				}
			} else if(nextPack.equals("setChunckCoordinates")){
				client.chunckCoordinates[0]=lineScanner.nextInt();
				client.chunckCoordinates[1]=lineScanner.nextInt();
			} else if(nextPack.equals("setMoveSpeed")){
				client.moveSpeed=lineScanner.nextInt();
			}
				
		}
		client.draw();
	}
}
