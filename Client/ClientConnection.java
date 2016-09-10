import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Scanner;

public class ClientConnection {
	private Client client;
	private DatagramSocket socket;
	private Listener listener;
	private String serverIp;
	public int connectionId;

	public ClientConnection(Client client,String serverIp){
		this.serverIp=serverIp;
		this.client=client;
		this.connectionId=0;
		try {
			this.socket=new DatagramSocket();
			listener=new Listener();
			listener.start();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send(String string){
		try {
			byte[] sendData = string.getBytes();
			System.out.println("Client: trying to send data of lenght: " + sendData.length);
			DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,new InetSocketAddress(InetAddress.getByName(serverIp), 3322));
			socket.send(sendPacket);
			System.out.println("Client Sent: " + string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//ServerListener
	class Listener extends Thread{

		public void run(){
			//Initialise the scanner listening
			try{
				byte[] receiveData=new byte[100];
				DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);

				while(socket!=null){
					socket.receive(receivePacket);
					processLine(new String((receivePacket.getData())));
				}

			} catch(IOException e){

			}
		}
	}

	private void processLine(String line){
		line = line.trim();
		System.out.println("Client received: " +line);
		Scanner lineScanner = new Scanner(line);
		while (lineScanner.hasNext()){
			String nextPack = lineScanner.next();

			//Scans for connection id, setting current connection id to this.
			if(nextPack.equals("setConnectionId")){
				if(lineScanner.hasNextInt()){
					this.connectionId=lineScanner.nextInt();
					client.status="Connected";
					client.advice="Use 'WASD' to move.";
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
		lineScanner.close();
		client.draw();
	}
	
	@SuppressWarnings("deprecation")
	public void close(){
		
		socket.close();
		socket=null;//Gets the listener out of its loop.
	}
	
}
