package me.davidnery.flappybird.threads;

import java.util.Iterator;
import java.util.List;

import me.davidnery.flappybird.entities.Bird;
import me.davidnery.flappybird.entities.Pipe;
import me.davidnery.flappybird.panels.GamePanel;

public class MainThread implements Runnable {
	
	private Thread thread;
	
	private boolean running;
	
	private final Bird bird;
	private final GamePanel panel;
	
	private final List<Pipe> pipes;
	
	private final double GRAVITY = 0.35;
	
	public MainThread(Bird bird, GamePanel panel) {		
		this.running = false;
		
		this.bird = bird;
		this.panel = panel;
		
		this.pipes = panel.getPipes();
	}
	
	public boolean isRunning() {
		return running;
	}

	public void start() {
		if(!running) {
			running = true;
			
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void stop() {
		if(running) {
			running = false;
			thread.interrupt();
		}
	}
	
	@Override
	public void run() {

		long lastUpdate = System.currentTimeMillis();

		while(running) {

			if(System.currentTimeMillis() - lastUpdate > 100) {
				bird.updateBirdStatus();
				lastUpdate = System.currentTimeMillis();
			}

			double varY = bird.getVelocity()*GRAVITY;
			bird.setY((int) (bird.getY() - varY));
			if(bird.getY() < 0) bird.setY(0);
			bird.setVelocity(bird.getVelocity() - GRAVITY);

			Iterator<Pipe> it = pipes.iterator();
			while(it.hasNext()) {
				Pipe p = it.next();
				p.setX(p.getX()-1);
				// 52 = width of pipe
				if(p.getX() == -52) {
					it.remove();
				} else if(!it.hasNext() && p.getX() == 150) {
					panel.addPipes();
					break;
				}
			}
			
			// Check if X coord of bird is more than X coord of last pipe
			if(pipes.size() >= 3 && bird.getX() == pipes.get(pipes.size()-3).getX()+52) {
				panel.updateScore();
				panel.playPoint();
			}
			
			if(panel.collided()) {
				panel.playHit();
				bird.setDead(true);
				panel.repaint();
				
				return;
			}
			
			panel.repaint();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}
