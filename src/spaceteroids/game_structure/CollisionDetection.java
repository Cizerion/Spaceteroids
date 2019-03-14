package spaceteroids.game_structure;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import spaceteroids.animated_sprites.ASBoss;
import spaceteroids.animated_sprites.ASBossClaw;
import spaceteroids.animated_sprites.ASEnemySpaceship;
import spaceteroids.animated_sprites.ASSpaceship;
import spaceteroids.animated_sprites.AnimatedSprite;
import spaceteroids.entity_generators.Asteroids;
import spaceteroids.entity_generators.Boosts;
import spaceteroids.sprite_shapes.BossShield;
import spaceteroids.sprite_shapes.Sprite;

public class CollisionDetection extends Thread {
	
	private double WIDTH;
	private double HEIGHT;
	private double enemyEntityCount;
    private SimpleTypeWrapper score;
    private SimpleTypeWrapper checkTimer;
    private ArrayList<AnimatedSprite> smallAsteroidList;
    private ArrayList<AnimatedSprite> mediumAsteroidList;
    private ArrayList<AnimatedSprite> largeAsteroidList;
    private ArrayList<Sprite> heartBoostList;
    private ArrayList<Sprite> laserList;
    private ArrayList<AnimatedSprite> bangList;
    private ArrayList<AnimatedSprite> bangTempList;
    private ArrayList<AnimatedSprite> bigBangList;
    private ArrayList<AnimatedSprite> bigBangTempList;
    private ArrayList<AnimatedSprite> hitList; 
    private ArrayList<AnimatedSprite> hitTempList;
    private MediaPlayer bangPlayer;
    private MediaPlayer bigBangPlayer;
    private MediaPlayer hitPlayer;
    private MediaPlayer shipHitPlayer;
    private MediaPlayer boostPlayer;
    double timeParam;
    private ASSpaceship ship;
    private ArrayList<ASBoss> boss;
    private ArrayList<ASEnemySpaceship> enemies;
    
    private AnimationTimer collision;
		
    public CollisionDetection() {
    	setName("Collision detection");
    	URL sound = getClass().getResource("/sounds/explosion/explodemini.wav");
    	bangPlayer = new MediaPlayer(new Media(sound.toString()));
    	sound = getClass().getResource("/sounds/explosion/explode.wav");
    	bigBangPlayer = new MediaPlayer(new Media(sound.toString()));
    	sound = getClass().getResource("/sounds/explosion/explosion01.wav");
    	hitPlayer = new MediaPlayer(new Media(sound.toString()));
    	sound = getClass().getResource("/sounds/events/FX010.mp3");
    	shipHitPlayer = new MediaPlayer(new Media(sound.toString()));;
    	sound = getClass().getResource("/sounds/events/FX056.mp3");
    	boostPlayer = new MediaPlayer(new Media(sound.toString()));;
    	setDaemon(true);
	}
    
    public CollisionDetection(double WIDTH, double HEIGHT, double enemyEntityCount,
    SimpleTypeWrapper score,
    SimpleTypeWrapper checkTimer,
    ArrayList<AnimatedSprite> smallAsteroidList,
    ArrayList<AnimatedSprite> mediumAsteroidList,
    ArrayList<AnimatedSprite> largeAsteroidList,
    ArrayList<Sprite> heartBoostList,
    ArrayList<Sprite> laserList,
    ArrayList<AnimatedSprite> bangList,
    ArrayList<AnimatedSprite> bangTempList,
    ArrayList<AnimatedSprite> bigBangList,
    ArrayList<AnimatedSprite> bigBangTempList,
    ArrayList<AnimatedSprite> hitList,
    ArrayList<AnimatedSprite> hitTempList,
    double timeParam,
    ASSpaceship ship,
    ArrayList<ASBoss> boss,
    ArrayList<ASEnemySpaceship> enemies) {
    	setName("Collision detection");
    	URL sound = getClass().getResource("/sounds/explosion/explodemini.wav");
    	bangPlayer = new MediaPlayer(new Media(sound.toString()));
    	sound = getClass().getResource("/sounds/explosion/explode.wav");
    	bigBangPlayer = new MediaPlayer(new Media(sound.toString()));
    	sound = getClass().getResource("/sounds/explosion/explosion01.wav");
    	hitPlayer = new MediaPlayer(new Media(sound.toString()));
    	sound = getClass().getResource("/sounds/events/FX010.mp3");
    	shipHitPlayer = new MediaPlayer(new Media(sound.toString()));;
    	sound = getClass().getResource("/sounds/events/FX056.mp3");
    	boostPlayer = new MediaPlayer(new Media(sound.toString()));;
		this.bangTempList = bangTempList;
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.enemyEntityCount = enemyEntityCount;
		this.score = score;
		this.checkTimer = checkTimer;
		this.smallAsteroidList = smallAsteroidList;
		this.mediumAsteroidList = mediumAsteroidList;
		this.largeAsteroidList = largeAsteroidList;
		this.heartBoostList = heartBoostList;
		this.laserList = laserList;
		this.bangList = bangList;
		this.bangTempList = bangTempList;
		this.bigBangList = bigBangList;
		this.bigBangTempList = bigBangTempList;
		this.hitList = hitList;
		this.hitTempList = hitTempList;
		this.timeParam = timeParam;
		this.ship = ship;
		this.boss = boss;
		this.enemies = enemies;
		setDaemon(true);
	}
	
    public void setParams(double WIDTH, double HEIGHT, double enemyEntityCount,
    	    SimpleTypeWrapper score,
    	    SimpleTypeWrapper checkTimer,
    	    ArrayList<AnimatedSprite> smallAsteroidList,
    	    ArrayList<AnimatedSprite> mediumAsteroidList,
    	    ArrayList<AnimatedSprite> largeAsteroidList,
    	    ArrayList<Sprite> heartBoostList,
    	    ArrayList<Sprite> laserList,
    	    ArrayList<AnimatedSprite> bangList,
    	    ArrayList<AnimatedSprite> bangTempList,
    	    ArrayList<AnimatedSprite> bigBangList,
    	    ArrayList<AnimatedSprite> bigBangTempList,
    	    ArrayList<AnimatedSprite> hitList,
    	    ArrayList<AnimatedSprite> hitTempList,
    	    double timeParam,
    	    ASSpaceship ship,
    	    ArrayList<ASBoss> boss,
    	    ArrayList<ASEnemySpaceship> enemies) {
		this.bangTempList = bangTempList;
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.enemyEntityCount = enemyEntityCount;
		this.score = score;
		this.checkTimer = checkTimer;
		this.smallAsteroidList = smallAsteroidList;
		this.mediumAsteroidList = mediumAsteroidList;
		this.largeAsteroidList = largeAsteroidList;
		this.heartBoostList = heartBoostList;
		this.laserList = laserList;
		this.bangList = bangList;
		this.bangTempList = bangTempList;
		this.bigBangList = bigBangList;
		this.bigBangTempList = bigBangTempList;
		this.hitList = hitList;
		this.hitTempList = hitTempList;
		this.timeParam = timeParam;
		this.ship = ship;
		this.boss = boss;
		this.enemies = enemies;
    }
    
    public void shipBoostCollisionDetection(double time, Sprite entityTemp, Iterator<Sprite> tempIter) {
    	if(ship.getHealth() < ship.getHealthAtStart() && ship.getFrame(time).intersectsShape(entityTemp)){
			boostSoundPlay();
			tempIter.remove();
			ship.increaseHealth();
		}
    }
    
	public void shipCollisionDetection(double time, AnimatedSprite entityTemp) {
		if(!(ship.isImmunity()) && ship.getFrame(time).intersectsShape(entityTemp.getFrame(time))){
			shipHitSoundPlay();
			ship.decreaseHealth();
			ship.setImmunity(true);
		}
        if(ship.getTime() >= 100) {
			ship.setTime(0);
			ship.setImmunity(false);
		}
	}
	
	public void shipCollisionDetection(double time, Sprite entityTemp) {
		if(!(ship.isImmunity()) && ship.getFrame(time).intersectsShape(entityTemp)){
			shipHitSoundPlay();
			ship.decreaseHealth();
			ship.setImmunity(true);
		}
        if(ship.getTime() >= 100) {
			ship.setTime(0);
			ship.setImmunity(false);
		}
	}
	
	public void shipCollisionDetection(double time, Sprite entityTemp, Iterator<Sprite> tempIter) {
		if(!(ship.isImmunity()) && ship.getFrame(time).intersectsShape(entityTemp)){
			shipHitSoundPlay();
			tempIter.remove();
			ship.decreaseHealth();
			ship.setImmunity(true);
		}
        if(ship.getTime() >= 100) {
			ship.setTime(0);
			ship.setImmunity(false);
		}
	}
	
	public void addEffect(ArrayList<AnimatedSprite> mainList, ArrayList<AnimatedSprite> buffList, double time, Sprite temp, double deltaX, double deltaY) {
		mainList.add(buffList.get(mainList.size()));
		mainList.get(mainList.size() - 1).setPosition(temp.getPositionX() + deltaX, temp.getPositionY() + deltaY);
		mainList.get(mainList.size() - 1).setTime(time);
	}
	
	public void addEffect(ArrayList<AnimatedSprite> mainList, ArrayList<AnimatedSprite> buffList, double time, Sprite temp, double deltaX, double deltaY, double velX, double velY) {
		mainList.add(buffList.get(mainList.size()));
		mainList.get(mainList.size() - 1).setPosition(temp.getPositionX() + deltaX, temp.getPositionY() + deltaY);
		mainList.get(mainList.size() - 1).setVelocity(velX, velY);
		mainList.get(mainList.size() - 1).setTime(time);
	}
	
	public void bangSoundPlay() {
		bangPlayer.setStartTime(Duration.ZERO);
		bangPlayer.seek(Duration.ZERO);
		bangPlayer.play();
	}
	
	public void bigBangSoundPlay() {
		bigBangPlayer.setStartTime(Duration.ZERO);
		bigBangPlayer.seek(Duration.ZERO);
		bigBangPlayer.play();
	}
	
	public void hitSoundPlay() {
		hitPlayer.setStartTime(Duration.ZERO);
		hitPlayer.seek(Duration.ZERO);
		hitPlayer.play();
	}
	
	public void shipHitSoundPlay() {
		shipHitPlayer.setStartTime(Duration.ZERO);
		shipHitPlayer.seek(Duration.ZERO);
		shipHitPlayer.play();
	}
	
	public void boostSoundPlay() {
		boostPlayer.setStartTime(Duration.ZERO);
		boostPlayer.seek(Duration.ZERO);
		boostPlayer.play();
	}
	
	public void run() {
			// collision detecting
			collision = new AnimationTimer() {
				
				public void handle(long currentNanoTime) {
					double time = (currentNanoTime - timeParam)/1000000000d;
					if(ship.isImmunity()) ship.setTime(ship.getTime() + 1);
					try {
						Iterator<Sprite> laserIter = laserList.iterator();
						while(laserIter.hasNext()){
				        	Sprite tempLaser = laserIter.next();
				        	if(tempLaser.getPositionX() >= (WIDTH - 40)) {
				        		tempLaser.setPosition(tempLaser.getPositionX(), -1 * (WIDTH - 40));
				        		laserIter.remove();
				        	}
				        	//enemy intersection with laser
				        	Iterator<ASEnemySpaceship> enemyIter = enemies.iterator();
				        	while(enemyIter.hasNext()){
				        		ASEnemySpaceship enemyTemp = enemyIter.next();
				        		if(tempLaser.intersectsShape(enemyTemp.getFrame(time))) {
				        			enemyTemp.decreaseHealth();
				        			if(enemyTemp.getHealth() <= 0) {
				        				addEffect(bangList, bangTempList, time, tempLaser, -80d, -128d);
				        				bangSoundPlay();
				        				enemyIter.remove();
				        				score.setIntValue(score.getIntValue() + (int)(200 + 10 * (enemies.size() + 1)));
				        			} else {
				        				addEffect(hitList, hitTempList, time, tempLaser, -20d, -50d);
					        			hitSoundPlay();
				        			}
				        			laserIter.remove();
				        		}
				        	}
				        	//boss intersection with laser
				        	Iterator<ASBoss> bossIter = boss.iterator();				        	
				        	while(bossIter.hasNext()) {
				        		ASBoss bossTemp = bossIter.next();
				        		Iterator<BossShield> shieldIter = bossTemp.getBossShield().iterator(); //boss shield intersection with laser
				        		while(shieldIter.hasNext()) {
				        			BossShield shieldTemp = shieldIter.next();
				        			if(tempLaser.intersectsShape(shieldTemp)) {
				        				addEffect(bangList, bangTempList, time, tempLaser, -128d, -128d, 33, 0);
				        				bangSoundPlay();
				        				laserIter.remove();
				        			}
				        		}
				        		Iterator<ASBossClaw> clawsIter = bossTemp.getBossClaws().iterator(); //boss claws intersection with laser
				        		while(clawsIter.hasNext()) {
				        			ASBossClaw clawTemp = clawsIter.next();
				        			if(!(clawTemp.getImmunity()) && tempLaser.intersectsShape(clawTemp.getFrame(time))) {
				        				clawTemp.decreaseHealth();
				        				if(clawTemp.getHealth() <= 0) {
					        				addEffect(bigBangList, bigBangTempList, time, tempLaser, -100d, -128d, 33, 0);
					        				bigBangSoundPlay();
						        			clawsIter.remove();
						        			ASBossClaw.decreaseCounter();
					        				score.setIntValue(1000 + (int)(score.getIntValue() * 1.5));
					        			} else {
					        				addEffect(hitList, hitTempList, time, tempLaser, 0d, -50d);
						        			hitSoundPlay();
					        			}
				        				laserIter.remove();
				        			}
				        			if(clawTemp.getImmunity() && tempLaser.intersectsShape(clawTemp.getFrame(time))){
				        				addEffect(bangList, bangTempList, time, tempLaser, -100d, -128d, 33, 0);
					        			bangSoundPlay();
					        			laserIter.remove();
				        			}
				        		}				        		
				        		if(bossTemp.getBossShield().isEmpty() && tempLaser.intersectsShape(bossTemp.getFrame(time))) { //boss body intersection with laser
				        			bossTemp.decreaseHealth();
				        			if(bossTemp.getHealth() <= 0) {
				        				addEffect(bigBangList, bigBangTempList, time, tempLaser, -100d, -128d, 33, 0);
				        				bigBangSoundPlay();
					        			bossIter.remove();
				        				score.setIntValue(2500 + (score.getIntValue() * 3));
				        			} else {
				        				addEffect(hitList, hitTempList, time, tempLaser, 0d, -50d);
					        			hitSoundPlay();
				        			}
				        			laserIter.remove();
				        		}
				        	}
				        	//small asteroid intersection with laser
				        	Iterator<AnimatedSprite> asteroidIter = smallAsteroidList.iterator();
				        	while(asteroidIter.hasNext()){
				        		AnimatedSprite asteroidTemp = asteroidIter.next();
				        		if(tempLaser.intersectsShape(asteroidTemp.getFrame(time))) {
				        			asteroidTemp.decreaseHealth();
				        			if(asteroidTemp.getHealth() <= 0) {
				        				addEffect(bangList, bangTempList, time, tempLaser, -100d, -128d);
					        			bangSoundPlay();
				        				asteroidIter.remove();
				        				score.setIntValue(score.getIntValue() + (int)(100 + 9 * (smallAsteroidList.size() + 1)));
				        			} else {
				        				addEffect(hitList, hitTempList, time, tempLaser, 0d, -50d);
					        			hitSoundPlay();
				        			}
				        			laserIter.remove();
				           		}
				        	}
				        	//medium asteroid intersection with laser
				        	asteroidIter = mediumAsteroidList.iterator();
				        	while(asteroidIter.hasNext()){
				        		AnimatedSprite asteroidTemp = asteroidIter.next();
				        		if(tempLaser.intersectsShape(asteroidTemp.getFrame(time))) {
				        			asteroidTemp.decreaseHealth();
				        			if(asteroidTemp.getHealth() <= 0) {
				        				addEffect(bangList, bangTempList, time, tempLaser, -128d, -128d);
					        			bangSoundPlay();
				        				asteroidIter.remove();
				        				score.setIntValue(score.getIntValue() + (int)(300 + 51 * (mediumAsteroidList.size() + 1)));
				        			} else {
				        				addEffect(hitList, hitTempList, time, tempLaser, -30d, -50d);
					        			hitSoundPlay();
				        			}
				        			laserIter.remove();
				        		}
				        	}
				        	//large asteroid intersection with laser
				        	asteroidIter = largeAsteroidList.iterator();
				        	while(asteroidIter.hasNext()){
				        		AnimatedSprite asteroidTemp = asteroidIter.next();
				        		if(tempLaser.intersectsShape(asteroidTemp.getFrame(time))) {
				        			addEffect(bangList, bangTempList, time, tempLaser, -128d, -128d);
				        			bangSoundPlay();
				        			laserIter.remove();
				        		}
				        	}
				        }
				      	//enemy intersection with ship
						Iterator<ASEnemySpaceship> enemyIter = enemies.iterator();
				        while(enemyIter.hasNext()){
				        	ASEnemySpaceship enemyTemp = enemyIter.next();
				        	enemyTemp.laserFireLogic();
				        	shipCollisionDetection(time, enemyTemp);
				        	if(enemyTemp.getFrame(time).getPositionX() <= -150)
				        		enemyIter.remove();
				        	Iterator<Sprite> laserEnemyIter = enemyTemp.getLaserList().iterator();
				        	while(laserEnemyIter.hasNext()) { //enemy laser intersection with ship
				        		Sprite tempEnemyLaser = laserEnemyIter.next();
				        		shipCollisionDetection(time, tempEnemyLaser, laserEnemyIter);
				        		Iterator<Sprite> laserSecondIter = laserList.iterator();
				        		while(laserSecondIter.hasNext()){ //enemy laser intersection with laser ship
				        			Sprite tempLaser = laserSecondIter.next();
				        			if(tempLaser.intersectsShape(tempEnemyLaser)) {
				        				laserSecondIter.remove();
				        				laserEnemyIter.remove();
				        			}
				        		}
				        	}
					    }
				        //boss intersection with ship
			        	Iterator<ASBoss> bossIter = boss.iterator();				        	
			        	while(bossIter.hasNext()) {
			        		ASBoss bossTemp = bossIter.next();
			        		Iterator<BossShield> shieldIter = bossTemp.getBossShield().iterator(); //boss shield intersection with ship
			        		while(shieldIter.hasNext()) {
			        			BossShield shieldTemp = shieldIter.next();
			        			shipCollisionDetection(time, shieldTemp);
			        		}
			        		Iterator<ASBossClaw> clawsIter = bossTemp.getBossClaws().iterator(); //boss claws intersection with ship
			        		while(clawsIter.hasNext()) {
			        			ASBossClaw clawTemp = clawsIter.next();
			        			shipCollisionDetection(time, clawTemp);
			        		}
			        		shipCollisionDetection(time, bossTemp);
			        	}
				        
				        //small asteroid intersection with ship
				        Iterator<AnimatedSprite> asteroidIter = smallAsteroidList.iterator();
				        while(asteroidIter.hasNext()){
				        	AnimatedSprite asteroidTemp = asteroidIter.next();
				        	if(asteroidTemp.getFrame(time).getPositionX() <= -100)
				        		asteroidIter.remove();
				        	shipCollisionDetection(time, asteroidTemp);
					    }
				        //medium asteroid intersection with ship
				        asteroidIter = mediumAsteroidList.iterator();
				        while(asteroidIter.hasNext()){
				        	AnimatedSprite asteroidTemp = asteroidIter.next();
				        	if(asteroidTemp.getFrame(time).getPositionX() <= -150)
				        		asteroidIter.remove();
				        	shipCollisionDetection(time, asteroidTemp);
				        }
				        //large asteroid intersection with ship
				        asteroidIter = largeAsteroidList.iterator();
				        while(asteroidIter.hasNext()){
				        	AnimatedSprite asteroidTemp = asteroidIter.next();
				        	if(asteroidTemp.getFrame(time).getPositionX() <= -200)
				        		asteroidIter.remove();
				        	shipCollisionDetection(time, asteroidTemp);
				        }
				        // heart boost intersection with ship
				        Iterator<Sprite> boostIter = heartBoostList.iterator();
				        while(boostIter.hasNext()) {
				        	Sprite boostTemp = boostIter.next();
				        	if(boostTemp.getPositionX() <= -50)
				        		boostIter.remove();
				        	shipBoostCollisionDetection(time, boostTemp, boostIter);
				        }
				        // check bang position
				        Iterator<AnimatedSprite> bangIter = bangList.iterator();
						Iterator<AnimatedSprite> bangTempIter = bangTempList.iterator();
						while(bangIter.hasNext()){
							AnimatedSprite tempBang = bangIter.next();
							bangTempIter.next();
							if(tempBang.getFrame(time).getImage() == null) {
								bangIter.remove();
								bangTempIter.remove();
				        	}
							if(tempBang.getFrame(time - tempBang.getTime()).getPositionX() <= -150 || tempBang.getFrame(time - tempBang.getTime()).getPositionX() >= (WIDTH + 50)) {
								bangIter.remove();
								bangTempIter.remove();
				        	}
						}
						// check big bang position
				        Iterator<AnimatedSprite> bigBangIter = bigBangList.iterator();
						Iterator<AnimatedSprite> bigBbangTempIter = bigBangTempList.iterator();
						while(bigBangIter.hasNext()){
							AnimatedSprite tempBang = bigBangIter.next();
							bigBbangTempIter.next();
							if(tempBang.getFrame(time).getImage() == null) {
								bigBangIter.remove();
								bigBbangTempIter.remove();
				        	}
							if(tempBang.getFrame(time - tempBang.getTime()).getPositionX() <= -150 || tempBang.getFrame(time - tempBang.getTime()).getPositionX() >= (WIDTH + 50)) {
								bigBangIter.remove();
								bigBbangTempIter.remove();
				        	}
						}
						// check hit position
						Iterator<AnimatedSprite> hitIter = hitList.iterator();
						Iterator<AnimatedSprite> hitTempIter = hitTempList.iterator();
						while(hitIter.hasNext()){
							AnimatedSprite tempHit = hitIter.next();
							hitTempIter.next();
							if(tempHit.getFrame(time).getImage() == null) {
								hitIter.remove();
								hitTempIter.remove();
							}
							if(tempHit.getFrame(time - tempHit.getTime()).getPositionX() >= WIDTH) {
								hitIter.remove();
								hitTempIter.remove();
				        	}
						}
						// generation new enemies and asteroids
						if(checkTimer.getDoubleValue() <= 200) {
							if((int)(checkTimer.getDoubleValue()) >= 1 && (int)(checkTimer.getDoubleValue()) % 25 == 0)
								enemyEntityCount += 0.0201;
							if(checkTimer.getDoubleValue() >= 60.5 && checkTimer.getDoubleValue() < 60.57)
								largeAsteroidList.add(Asteroids.randomAsteroid("large" , WIDTH, 100, WIDTH, 400));
							if(smallAsteroidList.size() < (int)(enemyEntityCount))
								smallAsteroidList.add(Asteroids.randomAsteroid("small" , WIDTH, 0, (2 * WIDTH), HEIGHT));
							if(mediumAsteroidList.size() < (int)(enemyEntityCount / 3))
								mediumAsteroidList.add(Asteroids.randomAsteroid("medium" , (2 * WIDTH), 0, (2 * WIDTH), HEIGHT));
							if(largeAsteroidList.size() < (int)(enemyEntityCount / 5))
								largeAsteroidList.add(Asteroids.randomAsteroid("large" , (4 * WIDTH), 0, (2 * WIDTH), HEIGHT));
							if(heartBoostList.size() < 2)
								heartBoostList.add(Boosts.boost("heart", (4 * WIDTH), 0, (2 * WIDTH), HEIGHT));
							if(enemies.size() < (int)(enemyEntityCount / 1.5))
								enemies.add(new ASEnemySpaceship(50, WIDTH, 0, (2 * WIDTH), HEIGHT));
						}
					}
					catch (IllegalStateException ile) {
						System.out.println("2 asteroids were shoted by 1 laser");
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			collision.start();
	}
	
	public void cleanupCollision(double time, double enemyEntityCount) {
		checkTimer.setDoubleValue(time);
		this.enemyEntityCount = enemyEntityCount;
		}
	public void startCollision() {collision.start();}
	public void stopCollision() {collision.stop();}
	public void stopCollisionDetectionThread() {interrupt();}
}
