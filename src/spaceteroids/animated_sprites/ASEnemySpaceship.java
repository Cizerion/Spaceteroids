package spaceteroids.animated_sprites;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.canvas.GraphicsContext;
import spaceteroids.sprite_shapes.EnemySpaceship;
import spaceteroids.sprite_shapes.Sprite;

public class ASEnemySpaceship extends AnimatedSprite {
	private ArrayList<Sprite> laserList;
	private int trajectory;
	private int healthAtStart;
	private double coef;
		
	public ASEnemySpaceship(int hlth, double minX, double minY, double maxX, double maxY) {
		super();
		trajectory = (int) (Math.random() * 2);
		coef = Math.random() * 360;
		laserList = new ArrayList<Sprite>();
		setHealth(hlth);
		healthAtStart = hlth;
		setDuration(0.075);
		setDefaultShipFrames();
		setTrajectory();
		setPosition((maxX * Math.random() + minX), (maxY * Math.random() + minY));
	}
	
	public ASEnemySpaceship(Sprite[] collection, int hlth, double minX, double minY, double maxX, double maxY) {
		super(collection);
		trajectory = (int) (Math.random() * 2);
		coef = Math.random();
		laserList = new ArrayList<Sprite>();
		setHealth(hlth);
		healthAtStart = hlth;
		setDuration(0.075);
		setTrajectory();
		setPosition((maxX * Math.random() + minX), (maxY * Math.random() + minY));
	}
	
	public void setTrajectory() {
		if(trajectory == 0) setVelocity(-30, Math.sin(coef) * 20);
		else setVelocity(-30, Math.cos(coef) * 20);
	}
	
	public int getTrajectory() {
		return trajectory;
	}
	
	public double getCoef() {
		return coef;
	}
	
	public static void setDefaultEnemyVelocity(ArrayList<ASEnemySpaceship> enemyList) {
		for(ASEnemySpaceship enemyTemp: enemyList) {
			double velocity = -30;
			if(enemyTemp.getTrajectory() == 0) enemyTemp.setVelocity(velocity, Math.sin(enemyTemp.getCoef()) * 20);
			else enemyTemp.setVelocity(velocity, Math.cos(enemyTemp.getCoef()) * 20);
			for(Sprite tempLaser: enemyTemp.getLaserList())
				tempLaser.setVelocity(-150, 0);
		}
	}

	public static ArrayList<ASEnemySpaceship> generateEnemySpaceships(int amount, int health, double minX, double minY, double maxX, double maxY){
		ArrayList<ASEnemySpaceship> enemies = new ArrayList<ASEnemySpaceship>();
		for(int i = 0; i < amount; i++)
			enemies.add(new ASEnemySpaceship(health, minX, minY, maxX, maxY));
		return enemies;
	}
	
	public static void regenerateEnemySpaceships(ArrayList<ASEnemySpaceship> enemies, int amount, int health, double minX, double minY, double maxX, double maxY){
		Iterator<ASEnemySpaceship> enemyIter = enemies.iterator();
        while(enemyIter.hasNext()){
        	enemyIter.next();
        	enemyIter.remove();
        }
        ArrayList<ASEnemySpaceship> e = generateEnemySpaceships(amount, health, minX, minY, maxX, maxY);
		while(enemies.size() != amount)
        	enemies.add(e.get(enemies.size()));
	}
	
	public void laserFireLogic() {
		setTime(getTime() + 1);
		if(laserList.size() < 5 && getTime() >= 40 && getPositionX() <= 560) {
			laserList.add(new Sprite("images/laser/laser-inv.png", 22, 5, false, false, getPositionX() + 5, getPositionY() + 13, -150, 0));
			setTime(0);
		}
		Iterator<Sprite> laserIter = laserList.iterator();
		while(laserIter.hasNext()){
        	Sprite tempLaser = laserIter.next();
        	if(tempLaser.getPositionX() <= (- 40))
        		laserIter.remove();
		}
	}
	
	public EnemySpaceship getFrame(double time) {
		return (EnemySpaceship) super.getFrame(time);
	}
			
	public void setDefaultShipFrames() {
		EnemySpaceship[] imageArray = new EnemySpaceship[4];
		for(int i = 0; i < imageArray.length; i++)
			imageArray[i] = new EnemySpaceship("images/enemies/ff" + (i+1) + "-min.png");
		setFrameSequence(imageArray);
	}
		
	public ArrayList<Sprite> getLaserList() {
		return laserList;
	}
	
	public void render(double time, GraphicsContext gc) {
		for(Sprite tempLaser: laserList)
			tempLaser.render(gc);
		gc.fillRect(getPositionX(), getPositionY() - 6, (getWidth() - 26) * (getHealth() / (double) healthAtStart), 2);
		getFrame(time).render(gc);
	}
	
	public void updateAndRender(double time, GraphicsContext gc) {
		coef+=0.02;
		for(Sprite tempLaser: laserList) {
			tempLaser.update(0.06);
			tempLaser.render(gc);
		}
		gc.fillRect(getPositionX(), getPositionY() - 6, (getWidth() - 26) * (getHealth() / (double) healthAtStart), 2);
		update(0.06);
		getFrame(time).render(gc);
	}
}
