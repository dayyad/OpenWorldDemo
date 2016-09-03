import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Client {
	private Graphics g;
	private JPanel p;
	private JFrame frame;
	private boolean gameStarted;
	public double x;
	public double y;
	public int toX;
	public int toY;
	private ClientConnection connection;

	public Client(){
		x=0;
		y=0;
		toX=10;
		toY=10;
		gameStarted=false;

		//Creates the jframe/
		frame = new JFrame("Client");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);

		this.p = new Panel(this);

		//Sets the graphics so that we can draw things.
		JButton button = new JButton("Press enter to join localhost.");
		frame.add(p);

		//Adds a key listener to the panel
		p.requestFocus();
		p.repaint();
		p.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				System.out.println(e.getKeyChar());
				if(!gameStarted){
					gameStarted=true;
					openConnection();

				}
				p.repaint();
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				//System.out.println(e.getKeyChar());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				//System.out.println(e.getKeyChar());
			}
		});

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				System.out.println(e.getActionCommand());
			}
		});
	}

	//Opens connection to server.
	public void openConnection(){

		Socket socket;
		try {
			socket = new Socket("localhost",2222);
			connection = new ClientConnection(this,socket);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void doJoin(){
		p.removeAll();
		p.repaint();
	}


	public static void main(String[] args) {
		new Client();
	}

}
