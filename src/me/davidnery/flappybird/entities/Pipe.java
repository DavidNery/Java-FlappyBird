package me.davidnery.flappybird.entities;

import me.davidnery.flappybird.interfaces.IEntity;

public class Pipe implements IEntity {

	private int x, y;

	private final int width, height;

	public Pipe(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
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

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

}
