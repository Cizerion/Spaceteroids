package spaceteroids.sprite_shapes;

import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

public class Spaceship extends Sprite {
	
	public Spaceship() {
		super();
	}
	
	public Spaceship(String imageLink) {
		super(imageLink);
	}
	
	public Spaceship(String imageLink, double imgWidth, double imgHeight, boolean pRatio, boolean smooth) {
		super(imageLink, imgWidth, imgHeight, pRatio, smooth);
	}
	
	public Spaceship(String imageLink, double posX, double posY, double velX, double velY) {
		super(imageLink, posX, posY, velX, velY);
	}
	
	public Spaceship(String imageLink, double imgWidth, double imgHeight, boolean pRatio, boolean smooth,
			double posX, double posY, double velX, double velY) {
		super(imageLink, imgWidth, imgHeight, pRatio, smooth, posX, posY, velX, velY);
	}
	
	public Shape getShape() {
		return new Ellipse(getPositionX() + getWidth()/2 + 7, getPositionY() + getHeight()/2 + 0.5, getWidth()/2 - 8, getHeight()/2 - 4);
	}
}
