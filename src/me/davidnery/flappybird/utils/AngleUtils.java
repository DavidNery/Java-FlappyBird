package me.davidnery.flappybird.utils;

import me.davidnery.flappybird.entities.Bird;

public class AngleUtils {
	
	public double calcAngle(Bird bird) {
		double angle = bird.getVelocity()*-5;
		
		return Math.toRadians(angle > 50 ? 50 : angle);
	}

}
