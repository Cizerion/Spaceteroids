package spaceteroids.animated_sprites;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import spaceteroids.sprite_shapes.Spaceship;
import spaceteroids.sprite_shapes.Sprite;

public class ASSpaceship extends AnimatedSprite {
	private boolean immunity;
	private Sprite steelHealthBar;
	private Sprite[] healthBar;
	private Sprite[] reloadBar;
	private ArrayList<Sprite> laserList;
	private MediaPlayer laserPlayer;
	private int healthAtStart;
	
	public ASSpaceship(int hlth) {
		super();
		immunity = false;
		laserList = new ArrayList<Sprite>();
    	URL sound = getClass().getResource("/sounds/firing/FX309.wav");
		laserPlayer = new MediaPlayer(new Media(sound.toString()));
		setHealth(hlth);
		healthAtStart = hlth;
		setDuration(0.075);
		healthBarInit();
		reloadBarInit();
		setDefaultShipFrames();
	}
	
	public ASSpaceship(Sprite[] collection, int hlth) {
		super(collection);
		immunity = false;
		laserList = new ArrayList<Sprite>();
    	URL sound = getClass().getResource("/sounds/firing/FX309.wav");
		laserPlayer = new MediaPlayer(new Media(sound.toString()));
		setHealth(hlth);
		healthAtStart = hlth;
		healthBarInit();
		reloadBarInit();
		setDuration(0.075);
	}
	
	public Spaceship getFrame(double time) {
		return (Spaceship) super.getFrame(time);
	}
	
	public void setDamagedShipFrames() {
		Spaceship[] imageArray = new Spaceship[4];
		for(int i = 0; i < imageArray.length; i++) {
			if((i % 2) == 0) imageArray[i] = new Spaceship("images/ship/fff" + (i+1) + "-min.png", 64, 29, true, true, getPositionX(), getPositionY(), getVelocityX(), getVelocityY());
			else imageArray[i] = new Spaceship("images/ship/damaged.png", 64, 29, true, true, getPositionX(), getPositionY(), getVelocityX(), getVelocityY());
		}
		setFrameSequence(imageArray);
	}
	
	public void setNotDamagedShipFrames() {
		Spaceship[] imageArray = new Spaceship[4];
		for(int i = 0; i < imageArray.length; i++) 
			imageArray[i] = new Spaceship("images/ship/fff" + (i+1) + "-min.png", 64, 29, true, true, getPositionX(), getPositionY(), getVelocityX(), getVelocityY());
		setFrameSequence(imageArray);
	}
		
	public void setDefaultShipFrames() {
		Spaceship[] imageArray = new Spaceship[4];
		for(int i = 0; i < imageArray.length; i++)
			imageArray[i] = new Spaceship("images/ship/fff" + (i+1) + "-min.png", 64, 29, true, true, 240, 250, 0, 0);
		setFrameSequence(imageArray);
	}

	public void setImmunity(boolean imnty) {
		immunity = imnty;
		if(immunity) setDamagedShipFrames();
		else setNotDamagedShipFrames();
	}
	
	public void reloadBarInit() {
		reloadBar = new Sprite[6];
		for(int i = 0; i < reloadBar.length; i++)
			reloadBar[i] = new Sprite("images/reload/r" + (reloadBar.length - i) + "-min.png");
	}
	
	public void healthBarInit() {
		steelHealthBar = new Sprite("images/healthbar/hb0.png");
		healthBar = new Sprite[5];
		for(int i = 0; i < healthBar.length; i++)
			healthBar[i] = new Sprite("images/healthbar/hb" + (healthBar.length - i) + "-min.png");
	}
	
	public void reloading(ArrayList<Sprite> laserTempList) {
		Iterator<Sprite> laserTempIter = laserTempList.iterator();
		while(laserTempIter.hasNext()){
			laserTempIter.next();
			laserTempIter.remove();
		}
	}
	
	public void laserSoundPlay() {
		laserPlayer.setStartTime(Duration.ZERO);
		laserPlayer.seek(Duration.ZERO);
		laserPlayer.play();
	}
	
	public void addLaser(ArrayList<Sprite> laserTempList) {
		laserList.add(laserTempList.get(laserTempList.size() - 1));
		laserTempList.remove(laserTempList.size() - 1);
		laserList.get(laserList.size() - 1).setPosition(getPositionX() + 50, getPositionY() + 13);
		laserSoundPlay();
	}
	
	public ArrayList<Sprite> getLaserList() {
		return laserList;
	}
	
	public int getHealthAtStart() {
		return healthAtStart;
	}
	
	public void render(double time, GraphicsContext gc) {
		for(Sprite tempLaser: laserList)
			tempLaser.render(gc);
		getFrame(time).render(gc);
	}
	
	public void updateAndRender(double time, GraphicsContext gc, ArrayList<Sprite> laserTempList) {
		for(Sprite tempLaser: laserList) {
			tempLaser.update(0.06);
			tempLaser.render(gc);
		}
		update(0.03);
		getFrame(time).render(gc);
		if(getHealth() >= 0 && getHealth() <= 100) gc.drawImage(healthBar[(getHealth()/25)].getImage(), 200, 20);
		else gc.drawImage(steelHealthBar.getImage(), 200, 20);
		gc.drawImage(reloadBar[laserTempList.size()].getImage(), 170, 60);
	}
	
	public boolean isImmunity() {return immunity;}
}
