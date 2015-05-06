package org.geomajas.gwt2.client.animation;

import java.util.logging.Logger;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt2.client.map.View;

public class KineticTrajectory implements Trajectory {
	

	private static Logger logger = Logger.getLogger("MapPresenterImpl");

	private View beginView;

	private double duration;

	private double initialSpeed;
	
	double distance;

	private Coordinate direction;
	
	double decay = -0.005;
	
	double minVelocity = 0.05;
	
	double delay = 100;

	public KineticTrajectory(View beginView, Coordinate direction, double initialSpeed) {
		this.initialSpeed = initialSpeed;
		this.duration = Math.log(minVelocity / this.initialSpeed) / this.decay;
		this.distance = (this.minVelocity - this.initialSpeed) / this.decay;
		this.beginView = beginView;
		this.direction = normalize(direction);
	}

	@Override
	public View getView(double progress) {		
		double easing = initialSpeed * (Math.exp((decay * progress) * duration) - 1) / (minVelocity - initialSpeed);
		double ds = distance * easing;
		if(Math.abs(((int)(progress*10) - progress*10)) < 0.01) {
			logger.info("kinetics "+progress + ",ds="+ds);
		}
		Coordinate c = add(beginView.getPosition(), mult(direction, ds));
		return new View(c, beginView.getResolution());
	}
	
	public double getDuration() {
		return duration;
	}

	private Coordinate add(Coordinate c1, Coordinate c2) {
		return new Coordinate(c2.getX() + c1.getX(), c2.getY() + c1.getY());
	}

	private Coordinate mult(Coordinate c, double factor) {
		return new Coordinate(factor * c.getX(), factor * c.getY());
	}

	private Coordinate normalize(Coordinate c) {
		double d = Math.sqrt(c.getX() * c.getX() + c.getY() * c.getY());
		return new Coordinate(c.getX() / d, c.getY() / d);
	}

}
