package spaceteroids.sprite_shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class MediumAsteroid extends Sprite {
	
	public MediumAsteroid() {
		super();
	}
	
	public MediumAsteroid(String imageLink) {
		super(imageLink);
	}
	
	public MediumAsteroid(String imageLink, double imgWidth, double imgHeight, boolean pRatio, boolean smooth) {
		super(imageLink, imgWidth, imgHeight, pRatio, smooth);
	}
	
	public MediumAsteroid(String imageLink, double posX, double posY, double velX, double velY) {
		super(imageLink, posX, posY, velX, velY);
	}
	
	public MediumAsteroid(String imageLink, double imgWidth, double imgHeight, boolean pRatio, boolean smooth,
			double posX, double posY, double velX, double velY) {
		super(imageLink, imgWidth, imgHeight, pRatio, smooth, posX, posY, velX, velY);
	}
	
	@Override
	public Shape getShape() {
		return new Circle(getPositionX() + getWidth()/2, getPositionY() + getHeight()/2, getHeight()/2 - 11);
	}
	
	@Override
	public void render(GraphicsContext gc) {
		gc.drawImage(getImage(), getPositionX(), getPositionY());
	}
}
