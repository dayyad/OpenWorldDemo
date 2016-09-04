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

	public ServerConnection(Server server,Socket socket){
		this.server=server;
		this.socket=socket;

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
		
	}
	
	//For setting up a player that has just connected.
	public void initPlayer(){
		
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
	}

}
