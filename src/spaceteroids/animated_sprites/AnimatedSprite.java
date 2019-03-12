package spaceteroids.animated_sprites;

import javafx.scene.canvas.GraphicsContext;
import spaceteroids.game_structure.Positioning;
import spaceteroids.game_structure.Visualisation;
import spaceteroids.sprite_shapes.Sprite;

public class AnimatedSprite implements Positioning, Visualisation{
	private Sprite[] frames;
	private double duration;
	private double startTime;
	private int health;
	
	public AnimatedSprite() {
		frames = null;
		duration = 0.100;
		startTime = 0;
	}
	
	public AnimatedSprite(Sprite[] collection) {
		frames = new Sprite[collection.length];
		duration = 0.100;
		startTime = 0;
		
		for(int i = 0; i < collection.length; i++) // Or System.arraycopy(collection, 0, frames, 0, collection.length);
			frames[i] = collection[i]; 
	}
	
	public Sprite getFrame(double time) {
		int index = (int)((time % (frames.length * duration))/duration);
		return frames[index];
	}
	
	public Sprite getFrameEachOnce(double time) {
		int index = (int)((time % (frames.length * duration))/duration);
		if((index > (frames.length - 2)) && (frames[index] != null)) {
			for(int i = 0; i < frames.length; i++) 
				frames[i].setNullImage();
		}
		return frames[index];
	}
	
	public void setFrameSequence(Sprite [] collection) {
		frames = new Sprite[collection.length];
		for(int i = 0; i < collection.length; i++) 
			frames[i] = collection[i];
	}
	
	public double getWidth() {
		return frames[0].getWidth();
	}
	
	public double getHeight() {
		return frames[0].getHeight();
	}
	
	public void setPosition(double x, double y) {
		for(Sprite t: frames)
			t.setPosition(x, y);
	}
	
	public double getPositionX() {
		return frames[0].getPositionX();
	}
	
	public double getPositionY() {
		return frames[0].getPositionY();
	}
	
	public void setVelocity(double x, double y) {
		for(int i = 0; i < frames.length; i++)
			frames[i].setVelocity(x, y);
	}
	
	public double getVelocityX() {
		return frames[0].getVelocityX();
	}
	
	public double getVelocityY() {
		return frames[0].getVelocityY();
	}
	
	public void addVelocity(double x, double y) {
		for(int i = 0; i < frames.length; i++)
			frames[i].addVelocity(x, y);
		}
	// render only first frame
	public void render(GraphicsContext gc) {
		getFrame(0).render(gc);
	}
	// render frames by time
	public void render(double time, GraphicsContext gc) {
		getFrame(time).render(gc);
	}
	
	public void update(double time) {
		for(int i = 0; i < frames.length; i++)
			frames[i].setPosition(frames[i].getPositionX() + frames[i].getVelocityX() * time, 
					frames[i].getPositionY() + frames[i].getVelocityY() * time);
	}
	public void decreaseHealth() {
		health -= 25;
	}
	
	public void decreaseHealth(int value) {
		health -= value;
	}
	
	public void increaseHealth() {
		health += 25;
	}
	
	public void increaseHealth(int value) {
		health += value;
	}
	
	public void setOneFrame(int frameIndex, String imgUrl) {
		frames[frameIndex] = new Sprite(imgUrl);
	}
	
	public void setTime(double time) {startTime = time;}
	public double getTime() {return startTime;}
	public void setDuration(double d) {duration = d;}
	public double getDuration() {return duration;}
	public int getHealth() {return health;}
	public void setHealth(int hlth) {health = hlth;}
}
