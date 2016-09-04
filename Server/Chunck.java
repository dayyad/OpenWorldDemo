import java.util.ArrayList;


//SERVERSIDE
public class Chunck {
	private final int id;
	private static int lastId=0;
	
	public Chunck(){
		this.id = lastId++;
	}
	
	public Integer getId(){
		return this.id;
	}
}
