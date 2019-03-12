package spaceteroids.sprite_shapes;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Boss extends Sprite {
	
	public Boss() {
		super();
	}
	
	public Boss(String imageLink) {
		super(imageLink);
	}
	
	public Boss(String imageLink, double imgWidth, double imgHeight, boolean pRatio, boolean smooth) {
		super(imageLink, imgWidth, imgHeight, pRatio, smooth);
	}
	
	public Boss(String imageLink, double posX, double posY, double velX, double velY) {
		super(imageLink, posX, posY, velX, velY);
	}
	
	public Boss(String imageLink, double imgWidth, double imgHeight, boolean pRatio, boolean smooth,
			double posX, double posY, double velX, double velY) {
		super(imageLink, imgWidth, imgHeight, pRatio, smooth, posX, posY, velX, velY);
	}

	public Shape getShape() {
		Rectangle s1 = new Rectangle(getPositionX() + 4, getPositionY() + 10, getWidth()/2 + 20, getHeight()/2 + 20);
		Rectangle s2 = new Rectangle(getPositionX() + 50, getPositionY() + 5, getWidth()/2 + 5, getHeight()/2 + 30);
		s1.setArcHeight(50);
		s1.setArcWidth(50);
		s2.setArcHeight(50);
		s2.setArcWidth(50);
		return Shape.union(s1, s2);
	}
}
