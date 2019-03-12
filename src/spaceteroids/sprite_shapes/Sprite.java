package spaceteroids.sprite_shapes;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import spaceteroids.game_structure.Positioning;
import spaceteroids.game_structure.Visualisation;
import javafx.scene.canvas.GraphicsContext;

public class Sprite implements Positioning, Visualisation{
	private Image image;
	private double positionX;
	private double positionY;
	private double velocityX;
	private double velocityY;
	private double width;
	private double height;
	
	public Sprite() {
		positionX = 0;
		positionY = 0;
		velocityX = 0;
		velocityY = 0;
	}
	
	public Sprite(String imageLink) {
		image = new Image(imageLink);
		width = image.getWidth();
		height = image.getHeight();
		positionX = 0;
		positionY = 0;
		velocityX = 0;
		velocityY = 0;
	}
	
	public Sprite(String imageLink, double imgWidth, double imgHeight, boolean pRatio, boolean smooth) {
		image = new Image(imageLink, imgWidth, imgHeight, pRatio, smooth);
		width = image.getWidth();
		height = image.getHeight();
		positionX = 0;
		positionY = 0;
		velocityX = 0;
		velocityY = 0;
	}
	
	public Sprite(String imageLink, double posX, double posY, double velX, double velY) {
		image = new Image(imageLink);
		width = image.getWidth();
		height = image.getHeight();
		positionX = posX;
		positionY = posY;
		velocityX = velX;
		velocityY = velY;
	}
	
	public Sprite(String imageLink, double imgWidth, double imgHeight, boolean pRatio, boolean smooth,
			double posX, double posY, double velX, double velY) {
		image = new Image(imageLink, imgWidth, imgHeight, pRatio, smooth);
		width = image.getWidth();
		height = image.getHeight();
		positionX = posX;
		positionY = posY;
		velocityX = velX;
		velocityY = velY;
	}
	
	public void setImage(String imageLink) {
		image = new Image(imageLink);
		width = image.getWidth();
		height = image.getHeight();
	}
	
	public void setNullImage() {
		image = null;
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setPosition(double x, double y) {
		positionX = x;
		positionY = y;
	}
	
	public double getPositionX() {
		return positionX;
	}
	
	public double getPositionY() {
		return positionY;
	}
	
	public void setVelocity(double x, double y) {
		velocityX = x;
		velocityY = y;
	}
	
	public double getVelocityX() {
		return velocityX;
	}
	
	public double getVelocityY() {
		return velocityY;
	}
	
	public void addVelocity(double x, double y) {
		velocityX += x;
		velocityY += y;
	}
	
	public void update(double time) {
		positionX += velocityX * time;
		positionY += velocityY * time;
	}
	
	public void render(GraphicsContext gc) {
		gc.drawImage(image, positionX, positionY);
	}

	public String toString() {
		return " Position: [" + positionX + "," + positionY + "]"
	+ " Velocity: [" + velocityX + "," + velocityY +"]";
	}

	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public Shape getShape() {
		return new Rectangle(positionX, positionY, width, height);
	}

	public boolean intersectsShape(Sprite s) {
		return (Shape.intersect(this.getShape(), s.getShape()).getBoundsInParent().getWidth() > 0);
	}
}
