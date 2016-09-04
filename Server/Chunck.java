import java.util.ArrayList;


//SERVERSIDE
public class Chunck {
	private final int id;
	private static int lastId=0;
	private int xPos;
	private int yPos;
	
	public Chunck(int x,int y){
		this.xPos=x;
		this.yPos=y;
		this.id = lastId++;
	}
	
	
	
	public int getxPos() {
		return xPos;
	}



	public void setxPos(int xPos) {
		this.xPos = xPos;
	}



	public int getyPos() {
		return yPos;
	}



	public void setyPos(int yPos) {
		this.yPos = yPos;
	}



	public Integer getId(){
		return this.id;
	}
}
