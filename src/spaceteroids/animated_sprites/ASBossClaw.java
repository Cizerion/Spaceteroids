package spaceteroids.animated_sprites;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.canvas.GraphicsContext;
import spaceteroids.sprite_shapes.BossClaw;
import spaceteroids.sprite_shapes.Sprite;

public class ASBossClaw extends AnimatedSprite {
	private int trajectory;
	private int healthAtStart;
	private double coef = 0;
	private static int counter = 1;
	private boolean immunity;
		
	public ASBossClaw(int hlth, double posX, double posY) {
		super();
		if((counter % 2) == 0) trajectory = 0;
		else trajectory = 1;
		setHealth(hlth);
		healthAtStart = hlth;
		setDuration(0.075);
		setDefaultClawFrames();
		setPosition(posX, posY);
		immunity = true;
		counter++;
	}
	
	public ASBossClaw(Sprite[] collection, int hlth, double posX, double posY) {
		super(collection);
		if((counter % 2) == 0) trajectory = 0;
		else trajectory = 1;
		setHealth(hlth);
		healthAtStart = hlth;
		setDuration(0.075);
		setPosition(posX, posY);
		immunity = true;
		counter++;
	}
	
	//Behavior
	public void setClawBehavior(ArrayList<ASEnemySpaceship> enemies) {
		if(getPositionX() <= 360 && counter == 3) {
			setTime(getTime() + 1);
			setImmunity(false);
			if(trajectory == 0) {
				setVelocity(0, -(Math.sin(coef) + Math.cos(coef)) * 35);
				if(getTime() >= 100 && getTime() < 225)
					addVelocity(-70, 0);
				if(getTime() >= 225 && getPositionX() <= 352) {
					addVelocity(70, 0);
					if(getPositionX() >= 350) {
						setTime(0);
						setPosition(350, getPositionY());
						enemies.add(new ASEnemySpaceship(50, getPositionX() + 50, getPositionY() + 70, 0, 0));
					}
				}
			} else {
				setVelocity(0, (Math.sin(coef) + Math.cos(coef)) * 35);
				if(getTime() >= 175 && getTime() < 300)
					addVelocity(-70, 0);
				if(getTime() >= 300 && getPositionX() <= 352) {
					addVelocity(70, 0);
					if(getPositionX() >= 350) {
						setTime(100);
						setPosition(350, getPositionY());
						enemies.add(new ASEnemySpaceship(50, getPositionX() + 50, getPositionY() + 70, 0, 0));
					}
				}
			}
		} else if(counter == 3)	setVelocity(-20, 0);
		else if(getPositionX() >= 375) {
				setVelocity(-90, Math.random() * 90);
				enemies.add(new ASEnemySpaceship(50, getPositionX() + 50, getPositionY() + 70, 0, 0));
				enemies.add(new ASEnemySpaceship(50, getPositionX() + 50, getPositionY() + 70, 0, 0));
				enemies.add(new ASEnemySpaceship(50, getPositionX() + 50, getPositionY() + 70, 0, 0));
		} else if(getPositionX() <= -10) setVelocity(90,  Math.random() * -90);
		else if(getPositionY() <= 0) setVelocity(Math.random() * 90, 90);
		else if(getPositionY() >= 425) setVelocity(Math.random() * -90, -90);
	}
	
	public void setImmunity(boolean imnty) {
		immunity = imnty;
	}
	
	public boolean getImmunity() {
		return immunity;
	}
	
	public static void resetCounter() {
		counter = 1;
	}
	
	public static void decreaseCounter() {
		counter -= 1;
	}
	
	public static ArrayList<ASBossClaw> generateBossClaws(int amount, int health, double posX, double posY){
		ArrayList<ASBossClaw> claws = new ArrayList<ASBossClaw>();
		for(int i = 0; i < amount; i++)
			claws.add(new ASBossClaw(health, posX, posY + (i * 1)));
		return claws;
	}
	
	public static void regenerateBossClaws(ArrayList<ASBossClaw> claws, int amount, int health, double posX, double posY){
		Iterator<ASBossClaw> clawIter = claws.iterator();
        while(clawIter.hasNext()){
        	clawIter.next();
        	clawIter.remove();
        }
        ArrayList<ASBossClaw> bc = generateBossClaws(amount, health, posX, posY);
		while(claws.size() != amount)
        	claws.add(bc.get(claws.size()));
	}
	
	public BossClaw getFrame(double time) {
		return (BossClaw) super.getFrame(time);
	}
			
	public void setDefaultClawFrames() {
		BossClaw[] imageArray = new BossClaw[6];
		for(int i = 0; i < imageArray.length; i++)
			imageArray[i] = new BossClaw("images/boss/claw/" + (i+1) + "-min.png");
		setFrameSequence(imageArray);
	}
	
	public void render(double time, GraphicsContext gc) {
		gc.fillRect(getPositionX() + 10, getPositionY() - 6, (getWidth() - 70) * (getHealth() / (double) healthAtStart), 8);
		gc.strokeRect(getPositionX() + 10, getPositionY() - 6, (getWidth() - 70) * (getHealth() / (double) healthAtStart), 8);
		getFrame(time).render(gc);
	}
	
	public void updateAndRender(double time, GraphicsContext gc, ArrayList<ASEnemySpaceship> enemies) {
		setClawBehavior(enemies);
		coef+=0.02;
		gc.fillRect(getPositionX() + 10, getPositionY() - 6, (getWidth() - 70) * (getHealth() / (double) healthAtStart), 8);
		gc.strokeRect(getPositionX() + 10, getPositionY() - 6, (getWidth() - 70) * (getHealth() / (double) healthAtStart), 8);
		update(0.06);
		getFrame(time).render(gc);
	}
	
	public String toString(){
		return "Position X: " + getPositionX();
	}
}