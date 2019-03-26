package spaceteroids.entity_generators;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import spaceteroids.animated_sprites.AnimatedSprite;
import spaceteroids.sprite_shapes.LargeAsteroid;
import spaceteroids.sprite_shapes.MediumAsteroid;
import spaceteroids.sprite_shapes.SmallAsteroid;
import spaceteroids.sprite_shapes.Sprite;

final public class Asteroids {
	public static AnimatedSprite randomAsteroid(String type, double minX, double minY, double maxX, double maxY) {
		//pref. values: maxX = 1900, minX = 500, maxY = 690, minY = - 80; 
		double px = maxX * Math.random() + minX;
		double py = maxY * Math.random() + minY;
	
		// fill array of asteroids sprites
		Sprite[] imageArrayAsteroids = new Sprite[8];
		String path = "";
		if(type.equals("small")) {
			switch((int) (0 + Math.random() * 6)) {
				case 0:
					path = "images/asteroids/small_asteroid/a/a ";
					break;
				case 1:
					path = "images/asteroids/small_asteroid/b/b ";
					break;
				case 2:
					path = "images/asteroids/small_asteroid/c/c ";
					break;
				case 3:
					path = "images/asteroids/small_asteroid/d/d ";
					break;
				case 4:
					path = "images/asteroids/small_asteroid/e/e ";
					break;
				case 5:
					path = "images/asteroids/small_asteroid/f/f ";
					break;
				default:
					path = "images/asteroids/small_asteroid/a/a ";	
			}
			for(int j = 0; j < imageArrayAsteroids.length; j++) {
				imageArrayAsteroids[j] = new SmallAsteroid(path + "(" + (j*2 + 1) + ")-min.png");
				imageArrayAsteroids[j].setPosition(px,py);
			}
		} else if(type.equals("medium")) {
			switch((int) (0 + Math.random() * 10)) {
				case 0:
					path = "images/asteroids/medium_asteroid/a/a ";
					break;
				case 1:
					path = "images/asteroids/medium_asteroid/b/b ";
					break;
				case 2:
					path = "images/asteroids/medium_asteroid/c/c ";
					break;
				case 3:
					path = "images/asteroids/medium_asteroid/d/d ";
					break;
				case 4:
					path = "images/asteroids/medium_asteroid/e/e ";
					break;
				case 5:
					path = "images/asteroids/medium_asteroid/f/f ";
					break;
				case 6:
					path = "images/asteroids/medium_asteroid/g/g ";
					break;
				case 7:
					path = "images/asteroids/medium_asteroid/h/h ";
					break;
				case 8:
					path = "images/asteroids/medium_asteroid/i/i ";
					break;
				case 9:
					path = "images/asteroids/medium_asteroid/j/j ";
					break;
				default:
					path = "images/asteroids/medium_asteroid/a/a ";	
			} 
			for(int j = 0; j < imageArrayAsteroids.length; j++) {
				imageArrayAsteroids[j] = new MediumAsteroid(path + "(" + (j*2 + 1) + ")-min.png", 72, 72, true, true);
				imageArrayAsteroids[j].setPosition(px,py);
			}
		} else {
			switch((int) (0 + Math.random() * 7)) {
				case 0:
					path = "images/asteroids/large_asteroid/a/a ";
					break;
				case 1:
					path = "images/asteroids/large_asteroid/b/b ";
					break;
				case 2:
					path = "images/asteroids/large_asteroid/c/c ";
					break;
				case 3:
					path = "images/asteroids/large_asteroid/d/d ";
					break;
				case 4:
					path = "images/asteroids/large_asteroid/e/e ";
					break;
				case 5:
					path = "images/asteroids/large_asteroid/f/f ";
					break;
				case 6:
					path = "images/asteroids/large_asteroid/g/g ";
					break;
				default:
					path = "images/asteroids/large_asteroid/a/a ";
			}
			for(int j = 0; j < imageArrayAsteroids.length; j++) {
				imageArrayAsteroids[j] = new LargeAsteroid(path + "(" + (j*2 + 1) + ")-min.png", 180, 180, true, true);
				imageArrayAsteroids[j].setPosition(px,py);
			}
		}
		AnimatedSprite asteroid = new AnimatedSprite(imageArrayAsteroids);
		if(type.equals("small")) {
			asteroid.setHealth(25);
		}
		if(type.equals("medium")) {
			asteroid.setHealth(75);
		}
		asteroid.setDuration(Math.random() * 0.05 + 0.05);
		return asteroid;
	}
	
	public static List<AnimatedSprite> generateRandomAsteroids(int amount, String type, double minX, double minY, double maxX, double maxY) {
		List<AnimatedSprite> asteroidList = new ArrayList<AnimatedSprite>(30);
		for (int i = 0; i < amount; i++) {
			asteroidList.add(randomAsteroid(type , minX, minY, maxX, maxY));
		}
    	return asteroidList;
	}
	
	public static void regenerateRandomAsteroids(List<AnimatedSprite> asteroidList, int amount, String type, double minX, double minY, double maxX, double maxY) {
		Iterator<AnimatedSprite> asteroidIter = asteroidList.iterator();
		while(asteroidIter.hasNext()) {
        	asteroidIter.next();
			asteroidIter.remove();
		}
		for(int i = 0; i < amount; i++) {
			asteroidList.add(randomAsteroid(type , minX, minY, maxX, maxY));
		}
	}
	
	public static void setDefaultAsteroidVelocity(List<AnimatedSprite> asteroidList, double t) {
		for(AnimatedSprite asteroidTemp: asteroidList) {
			double velocity = -6 / asteroidTemp.getDuration();
			asteroidTemp.setVelocity(velocity, Math.sin(t + ((asteroidTemp.getDuration() * 4000) - 200)) * 30);
		}
	}
}
