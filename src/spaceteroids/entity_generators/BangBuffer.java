package spaceteroids.entity_generators;
import java.util.ArrayList;

import spaceteroids.animated_sprites.AnimatedSprite;
import spaceteroids.sprite_shapes.Sprite;

public class BangBuffer extends Thread {
	
	private volatile boolean isAlive;
	private ArrayList<AnimatedSprite> bangTempList;
	private ArrayList<AnimatedSprite> bigBangTempList;
	private ArrayList<AnimatedSprite> hitTempList;
	private ArrayList<Sprite> laserTempList;
		
	public BangBuffer(ArrayList<AnimatedSprite> bangList, ArrayList<AnimatedSprite> bigBangList ,ArrayList<Sprite> laserList, ArrayList<AnimatedSprite> hitList) {
		setName("Bang buffer");
		isAlive = true;
		laserTempList = laserList;
		bangTempList = bangList;
		bigBangTempList = bigBangList;
		hitTempList = hitList;
		setDaemon(true);
		start();
	}
	
	public void run() {
		while(isAlive){
			if(laserTempList.size() == 0) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				while(laserTempList.size() != 5) {
					Sprite laser = new Sprite("images/laser/laser-min.png");
					laser.setVelocity(150, 0);
					laserTempList.add(laser);
				}
			}
			if(bangTempList.size() < 10) {
    			Sprite[] temp = new Sprite[44];
    	        for(int i = 0; i < temp.length - 2; i++)
    	        	temp[i] = new Sprite("images/explosion/exp (" + (i+1) + ")-min.png");
    	        for(int i = 42; i < temp.length; i++)
    	        	temp[i] = new Sprite("images/explosion/exp (42)-min.png");
    	        AnimatedSprite bang = new AnimatedSprite(temp);
    			bang.setVelocity(-66, 0);
    			bang.setDuration(0.05);
    			bangTempList.add(bang);
			}
			if(hitTempList.size() < 8) {
    			Sprite[] temp = new Sprite[16];
    	        for(int i = 0; i < temp.length - 2; i++)
    	        	temp[i] = new Sprite("images/hit/hit (" + (i+1) + ")-min.png");
    	        for(int i = 14; i < temp.length; i++)
    	        	temp[i] = new Sprite("images/hit/hit (14)-min.png");
    	        AnimatedSprite hit = new AnimatedSprite(temp);
    	        hit.setVelocity(20, 0);
    	        hit.setDuration(0.06);
    			hitTempList.add(hit);
			}
			if(bigBangTempList.size() < 2) {
    			Sprite[] temp = new Sprite[63];
    	        for(int i = 0; i < temp.length; i++)
    	        	temp[i] = new Sprite("images/explosion/boss/bexp (" + (i+1) + ")-min.png");
    	        AnimatedSprite bang = new AnimatedSprite(temp);
    			bang.setVelocity(-66, 0);
    			bang.setDuration(0.02);
    			bigBangTempList.add(bang);
			}
		}
		System.out.println("BangBuffer finished"); // if thread is not daemon and closed by stopBangBuffer() it will be print
	}
	
	public void stopBangBuffer() {
		isAlive = false;
	}
}
