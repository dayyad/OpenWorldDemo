import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Panel extends JPanel {
	private Client client;
	public Panel(Client client){
		this.client=client;
	}

	public void paintComponent(Graphics g){
		//Clears graphics panel
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		//Draws status and advice bars.
		g.setColor(Color.red);
		g.drawString("Status: " + client.status, 0, 20);
		g.drawString(client.players.size() + " Players online.", 150, 20);
		g.setColor(Color.green);
		g.drawString(client.advice, 0, 40);
		
		//Draws world and player/s
		for(Player player : client.players){
			//A bit of math to make sure the oval is centered.
			//Player is drawn red, the other players drawn green
			if(player.equals(client.player))
				g.setColor(Color.red);
			else 
				g.setColor(Color.green);
			g.fillOval(player.getX(), player.getY(), player.getWidth(), player.getHeight());
		}
		
		//TODO draw world
	}
}
