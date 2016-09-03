import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
	private ArrayList<ServerConnection> clients;
	private ServerSocket serverSocket;

	public Server(){
		clients = new ArrayList<ServerConnection>();

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

	public static void main(String[] args) {
		new Server();

	}

}
