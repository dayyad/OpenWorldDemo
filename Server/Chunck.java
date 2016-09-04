import java.util.ArrayList;


//SERVERSIDE
public class Chunck {
	private ArrayList<Player> players;
	private final int id;
	private static int lastId=0;
	
	public Chunck(){
		players = new ArrayList<Player>();
		this.id = lastId++;
	}
	
	public void addPlayer(Player player){
		if(!players.contains(player))
			players.add(player);
	}
	
	public void removePlayer(Player player){
		if(players.contains(player))
			players.remove(player);
	}
}
