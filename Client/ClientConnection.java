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

		listener=new Listener();
		listener.start();
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
			if(nextPack.equals("setConnectionId"))
				if(lineScanner.hasNextInt())
					this.connectionId=scanner.nextInt();
			
			//Scans player packets and updates the local player data.
			if(nextPack.equals("setPlayer")){
				int x = lineScanner.nextInt();
				int y = lineScanner.nextInt();
				int width = lineScanner.nextInt();
				int height = lineScanner.nextInt();
				int id = lineScanner.nextInt();
				
				boolean found = false;
				for(Player player : client.players){
					//If the ids match, make the clients player equal to this player.
					if(player.getId()==connectionId)
						client.player=player;
					if(player.getId()==id)
						player.setX(x);
						player.setY(y);
						player.setWidth(width);
						player.setHeight(height);
						found=true;
				}
				if(!found){
					client.players.add(new Player(x,y,width,height,id));
				}
			}
				
		}
		client.draw();
	}
}
