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

	public ClientConnection(Client client,Socket socket){
		this.client=client;
		this.socket=socket;

		listener=new Listener();
		listener.start();
	}

	public void send(){
		try {
			writer=new PrintWriter(socket.getOutputStream());

			writer.println("TESTING TESTING TESTING");
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	}
}
