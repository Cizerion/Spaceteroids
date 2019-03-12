package spaceteroids.game_structure;

import javafx.scene.canvas.GraphicsContext;

public interface Visualisation {
	public void render(GraphicsContext gc);
	public double getWidth();
	public double getHeight();
}
