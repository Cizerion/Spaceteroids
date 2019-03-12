package spaceteroids.entity_generators;

import java.util.ArrayList;
import java.util.Iterator;

import spaceteroids.sprite_shapes.Sprite;

final public class Boosts {
	public static Sprite boost(String type, double minX, double minY, double maxX, double maxY) {
		Sprite boost;
		double px = maxX * Math.random() + minX;
		double py = maxY * Math.random() + minY;
		
		switch(type) {
		case "heart":
			boost = new Sprite("images/healthbar/heart3.png");
			boost.setPosition(px, py);
			return boost;
		default:
			boost = new Sprite("images/healthbar/dark_heart.png");
			boost.setPosition(px, py);
			return boost;
		}
	}
	
	public static ArrayList<Sprite> generateBoosts(int amount, String type, double minX, double minY, double maxX, double maxY) {
		ArrayList<Sprite> boostList = new ArrayList<Sprite>();
		for (int i = 0; i < amount; i++) 
			boostList.add(boost(type, minX, minY, maxX, maxY));
    	return boostList;
	}
	
	public static void setBoostsVelocity(ArrayList<Sprite> boostList, double velocityX, double velocityY) {
		for(Sprite boostTemp: boostList)
			boostTemp.setVelocity(velocityX, velocityY);
	}
	
	public static void regenerateBoosts(ArrayList<Sprite> boostList, int amount, String type, double minX, double minY, double maxX, double maxY) {
		Iterator<Sprite> boostIter = boostList.iterator();
		while(boostIter.hasNext()) {
			boostIter.next();
			boostIter.remove();
		}
		for(int i = 0; i < amount; i++) 
			boostList.add(boost(type, minX, minY, maxX, maxY));
	}
}
