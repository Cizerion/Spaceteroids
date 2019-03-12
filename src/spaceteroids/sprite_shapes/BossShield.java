package spaceteroids.sprite_shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class BossShield extends Sprite {
	
	public BossShield() {
		super();
	}
	
	public BossShield(String imageLink) {
		super(imageLink);
	}
	
	public BossShield(String imageLink, double imgWidth, double imgHeight, boolean pRatio, boolean smooth) {
		super(imageLink, imgWidth, imgHeight, pRatio, smooth);
	}
	
	public BossShield(String imageLink, double posX, double posY, double velX, double velY) {
		super(imageLink, posX, posY, velX, velY);
	}
	
	public BossShield(String imageLink, double imgWidth, double imgHeight, boolean pRatio, boolean smooth,
			double posX, double posY, double velX, double velY) {
		super(imageLink, imgWidth, imgHeight, pRatio, smooth, posX, posY, velX, velY);
	}
	
	@Override
	public Shape getShape() {
		return new Circle(getPositionX() + 57, getPositionY() + 39, getHeight()/2 - 7);
	}
	
	@Override
	public void render(GraphicsContext gc) {
		gc.drawImage(getImage(), getPositionX() - 20, getPositionY() - 38);
	}
}
