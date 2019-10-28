package me.davidnery.flappybird.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import me.davidnery.flappybird.entities.Bird;
import me.davidnery.flappybird.panels.GamePanel;
import me.davidnery.flappybird.threads.MainThread;

public class BirdClickListener implements MouseListener {

	private final Bird bird;

	private final MainThread mainThread;
	
	private final GamePanel panel;

	public BirdClickListener(Bird bird, MainThread mainThread, GamePanel panel) {
		this.bird = bird;

		this.mainThread = mainThread;
		
		this.panel = panel;
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(!mainThread.isRunning()) {
			panel.restart();
			panel.addPipes();
			mainThread.start();
		}

		panel.playWing();
		bird.setVelocity(10);

	}



}
