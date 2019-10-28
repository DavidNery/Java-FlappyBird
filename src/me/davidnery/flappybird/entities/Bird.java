package me.davidnery.flappybird.entities;

import me.davidnery.flappybird.enums.BirdStatus;
import me.davidnery.flappybird.interfaces.IEntity;

public class Bird implements IEntity {

	private int x, y;
	
	private double velocity;

	private final int width, height;
	
	private BirdStatus birdStatus;
	
	private boolean dead;

	public Bird(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.velocity = 0;
		this.width = width;
		this.height = height;
		
		this.birdStatus = BirdStatus.MID;
		
		this.dead = true;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int getY() {
		return this.y;
	}
	
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	
	public double getVelocity() {
		return velocity;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}
	
	public BirdStatus getBirdStatus() {
		return birdStatus;
	}
	
	public void updateBirdStatus() {
		switch(birdStatus) {
		case DOWN:
			this.birdStatus = BirdStatus.MID;
			break;
		case MID:
			this.birdStatus = BirdStatus.UP;
			break;
		case UP:
			this.birdStatus = BirdStatus.DOWN;
			break;
		}
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
	public boolean isDead() {
		return dead;
	}

}
