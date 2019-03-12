package spaceteroids.game_structure;

public interface Positioning {
	public void setPosition(double x, double y);
	public double getPositionX();
	public double getPositionY();
	public void setVelocity(double x, double y);
	public double getVelocityX();
	public double getVelocityY();
	public void addVelocity(double x, double y);
	public void update(double time);
}
