import java.awt.Color;
import java.awt.Font;
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
		if(client.connection!=null){
			g.drawString("Connection ID: "+ client.connection.connectionId, 250, 20);
		} if(client.player!=null){
			g.drawString("Currently in chunck: "+client.player.getChunckId(), 400, 20);
			g.drawString("Coordinates x: " + client.chunckCoordinates[0] + " y: " + client.chunckCoordinates[1], 400, 40);
			g.setColor(Color.blue);
			g.setFont(new Font("serif", Font.PLAIN, 10));
			g.drawString("Some of following features yet to be added.", 580, 20);
			g.drawString("Press 'M' to send message", 580, 40);
		}
		g.setColor(Color.green);
		g.drawString(client.advice, 0, 40);

		//Draws world and player/s
		for(Player player : client.players){
			//A bit of math to make sure the oval is centered.
			//Player is drawn red, the other players drawn green

			//Only draw players if in the same chunck.
			if(player.getChunckId()==client.player.getChunckId()){
				if(player.equals(client.player))
					g.setColor(Color.red);
				else
					g.setColor(Color.green);
				g.fillOval(player.getX(), player.getY(), player.getWidth(), player.getHeight());
			}
		}

		//TODO draw world
	}
}
