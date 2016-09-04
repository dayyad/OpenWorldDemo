
public class Player {
	private int x;
	private int y;
	private final int id;
	
	private int width;
	private int height;
	
	public Player(int x,int y,int width, int height,int id){
		this.id = id;
		
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	
	public int getId(){
		return id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
}
