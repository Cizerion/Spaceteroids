package spaceteroids.game_structure;

import javafx.scene.canvas.GraphicsContext;
import spaceteroids.sprite_shapes.Sprite;

public class LoopableBackground {
	private Sprite[] background;
	
	public LoopableBackground(String imageLink, double posX, double posY, double velX, double velY) {
		background = new Sprite[2];
		background[0] = new Sprite(imageLink, posX, posY, velX, velY);
		background[1] = new Sprite(imageLink, posX + background[0].getWidth(), posY, velX, velY);
	}
	
	public void setPosition(double x, double y) {
		background[0].setPosition(x, y);
		background[1].setPosition(x + background[0].getWidth(), y);
	}
	
	public void setVelocity(double x, double y) {
		for(Sprite t: background)
			t.setVelocity(x, y);
	}
	
	public void addVelocity(double x, double y) {
		for(Sprite t: background)
			t.addVelocity(x, y);
	}
	
	public void update(double time) {
		if(background[0].getPositionX() <= (-1 * (background[0].getWidth() + 76)))
			background[0].setPosition(background[1].getPositionX() + background[1].getWidth(), background[1].getPositionY());
		if(background[1].getPositionX() <= (-1 * (background[1].getWidth() + 76)))
			background[1].setPosition(background[0].getPositionX() + background[0].getWidth(), background[0].getPositionY());
		for(int i = 0; i < background.length; i++)
			background[i].setPosition(background[i].getPositionX() + background[i].getVelocityX() * time, 
					background[i].getPositionY() + background[i].getVelocityY() * time);
	}
	
	public void render(GraphicsContext gc) {
		gc.drawImage(background[0].getImage(), background[0].getPositionX(), background[0].getPositionY());
		gc.drawImage(background[1].getImage(), background[1].getPositionX(), background[1].getPositionY());
	}
}
