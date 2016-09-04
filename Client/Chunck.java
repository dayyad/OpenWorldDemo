import java.util.ArrayList;


//CLIENT SIDE
public class Chunck {
	private ArrayList<Player> players;
	private final int id;
	
	public Chunck(int id){
		this.id = id;
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
