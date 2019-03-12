package spaceteroids.game_structure;

import java.io.Serializable;

public class ScoreBoard implements Serializable {
	private static final long serialVersionUID = 4099481659804179424L;
	private String name;
	private String score;
	
	public ScoreBoard(String name, int score) {
		this.name = name;
		this.score = String.valueOf(score);
	}
	
	public ScoreBoard(String name, String score) {
		this.name = name;
		this.score = score;
	}
	
	public void setName(String name) {this.name = name;}
	public String getName() {return name;}
	public void setScore(int score) {this.score = String.valueOf(score);}
	public void setScore(String score) {this.score = String.valueOf(score);}
	public String getScore() {return score;}
	public int getIntScore() {return Integer.valueOf(score);}
}
