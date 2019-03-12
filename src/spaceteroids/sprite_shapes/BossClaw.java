package spaceteroids.sprite_shapes;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class BossClaw extends Sprite {
		
	public BossClaw() {
		super();
	}
	
	public BossClaw(String imageLink) {
		super(imageLink);
	}
	
	public BossClaw(String imageLink, double imgWidth, double imgHeight, boolean pRatio, boolean smooth) {
		super(imageLink, imgWidth, imgHeight, pRatio, smooth);
	}
	
	public BossClaw(String imageLink, double posX, double posY, double velX, double velY) {
		super(imageLink, posX, posY, velX, velY);
	}
	
	public BossClaw(String imageLink, double imgWidth, double imgHeight, boolean pRatio, boolean smooth,
			double posX, double posY, double velX, double velY) {
		super(imageLink, imgWidth, imgHeight, pRatio, smooth, posX, posY, velX, velY);
	}

	public Shape getShape() {
		Rectangle s1 = new Rectangle(getPositionX() + 7, getPositionY() + 5, getWidth()/2 + 40, getHeight()/2 + 62);
		Rectangle s2 = new Rectangle(getPositionX() + 100, getPositionY() + 47, getWidth()/2, getHeight()/2 - 22);
		s1.setArcHeight(120);
		s1.setArcWidth(120);
		return Shape.union(s1, s2);
	}
}
