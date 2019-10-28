package me.davidnery.flappybird;

import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JFrame;

import me.davidnery.flappybird.panels.GamePanel;

public class Main extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public Main() {
		super("Java FlappyBird");

		try {
			add(new GamePanel());
		} catch (IOException e) {
			e.printStackTrace();
		}

		setResizable(false);

		pack();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			new Main();
		});
	}

}
