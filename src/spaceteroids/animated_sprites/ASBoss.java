package spaceteroids.animated_sprites;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.canvas.GraphicsContext;
import spaceteroids.entity_generators.Asteroids;
import spaceteroids.sprite_shapes.Boss;
import spaceteroids.sprite_shapes.BossShield;
import spaceteroids.sprite_shapes.Sprite;

public class ASBoss extends AnimatedSprite {
	private ArrayList<ASBossClaw> bossClaws;
	private int healthAtStart;
	private ArrayList<BossShield> bossShield;
	private double coef = 0;
	
	public ASBoss(int hlth, double posX, double posY) {
		super();
		bossClaws = ASBossClaw.generateBossClaws(2, hlth, posX - 250, posY - 25);
		bossShield = new ArrayList<BossShield>();
		BossShield shield = new BossShield("images/boss/shield/shield.png");
		shield.setPosition(posX, posY);
		bossShield.add(shield);
		setHealth(hlth);
		healthAtStart = hlth;
		setDuration(0.075);
		setDefaultBossFrames();
		setPosition(posX, posY);
	}
	
	public ASBoss(Sprite[] collection, int hlth, double posX, double posY) {
		super(collection);
		bossClaws = ASBossClaw.generateBossClaws(2, hlth, posX - 250, posY - 25);
		bossShield = new ArrayList<BossShield>();
		BossShield shield = new BossShield("images/boss/shield/shield.png");
		shield.setPosition(posX, posY);
		bossShield.add(shield);
		setHealth(hlth);
		healthAtStart = hlth;
		setDuration(0.075);
		setPosition(posX, posY);
	}
	
	public Boss getFrame(double time) {
		return (Boss) super.getFrame(time);
	}
	
	public ArrayList<ASBossClaw> getBossClaws(){
		return bossClaws;
	}
	
	public ArrayList<BossShield> getBossShield(){
		return bossShield;
	}
		
	public void setDefaultBossFrames() {
		Boss[] imageArray = new Boss[2];
		for(int i = 0; i < imageArray.length; i++)
			imageArray[i] = new Boss("images/boss/f" + (i+1) + "-min.png");
		setFrameSequence(imageArray);
	}
	
	public static ArrayList<ASBoss> generateBoss(int amount, int health, double posX, double posY){
		ArrayList<ASBoss> boss = new ArrayList<ASBoss>();
		for(int i = 0; i < amount; i++)
			boss.add(new ASBoss(health, posX, posY));
		return boss;
	}
	
	public static void regenerateBoss(ArrayList<ASBoss> boss, int amount, int health, double posX, double posY) {
		ASBossClaw.resetCounter();
		Iterator<ASBoss> bossIter = boss.iterator();
        while(bossIter.hasNext()){
        	bossIter.next();
        	bossIter.remove();
        }
        ArrayList<ASBoss> bs = generateBoss(amount, health, posX, posY);
		while(boss.size() != amount)
			boss.add(bs.get(boss.size()));
	}
	
	//Behavior
	public void setBossBehavior(ArrayList<AnimatedSprite> smallAsteroidList) {
		setTime(getTime() + 1);
		if(bossClaws.size() == 0)
			bossShield.clear();
		if(getPositionX() <= 440) {
			if((int)(getTime()) >= 100) {
				int astCount = (bossClaws.size() == 0) ? 180 : 30;
				for(int i = 0; i < astCount; i += 30) {
					AnimatedSprite ast = Asteroids.randomAsteroid("small" , getPositionX(), getPositionY(), 0, 0);
					ast.setVelocity(Math.sin(i), Math.cos(i));
					smallAsteroidList.add(ast);
				}
				setTime(0);
			}
			if(!(bossShield.isEmpty()))
				for(BossShield tempShield: bossShield) 
					tempShield.setVelocity(0, -(Math.sin(coef) + Math.cos(coef)) * 30);
			setVelocity(0, -(Math.sin(coef) + Math.cos(coef)) * 30);
		} else {
			if(!(bossShield.isEmpty()))
				for(BossShield tempShield: bossShield) 
					tempShield.setVelocity(-20, 0);
			setVelocity(-20, 0);
		}
	}
	
	public void render(double time, GraphicsContext gc) {
		for(ASBossClaw tempClaw: bossClaws)
			tempClaw.render(time, gc);
		gc.fillRect(getPositionX() + 10, getPositionY() - 10, (getWidth() - 35) * (getHealth() / (double) healthAtStart), 8);
		gc.strokeRect(getPositionX() + 10, getPositionY() - 10, (getWidth() - 35) * (getHealth() / (double) healthAtStart), 8);
		getFrame(time).render(gc);
		for(BossShield tempShield: bossShield)
			tempShield.render(gc);
	}
	
	public void updateAndRender(double time, GraphicsContext gc, ArrayList<ASEnemySpaceship> enemies, ArrayList<AnimatedSprite> smallAsteroidList) {
		setBossBehavior(smallAsteroidList);
		coef+=0.02075;
		for(ASBossClaw tempClaw: bossClaws)
			tempClaw.updateAndRender(time, gc, enemies);
		gc.fillRect(getPositionX() + 10, getPositionY() - 10, (getWidth() - 35) * (getHealth() / (double) healthAtStart), 8);
		gc.strokeRect(getPositionX() + 10, getPositionY() - 10, (getWidth() - 35) * (getHealth() / (double) healthAtStart), 8);
		update(0.06);
		getFrame(time).render(gc);
		for(BossShield tempShield: bossShield) {
			tempShield.update(0.06);
			tempShield.render(gc);
		}
	}
	
	public String toString(){
		return "Position X: " + getPositionX();
	}
}
