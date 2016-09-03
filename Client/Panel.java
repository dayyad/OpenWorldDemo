import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Panel extends JPanel {
	private Client client;
	public Panel(Client client){
		this.client=client;
	}

	public void paintComponent(Graphics g){
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.red);
		g.drawRect(5, 5, client.toX	, client.toY);
	}
}
