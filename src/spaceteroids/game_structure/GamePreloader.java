package spaceteroids.game_structure;
import java.net.URL;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GamePreloader extends Preloader {
	
	private static final double WIDTH = 582;
    private static final double HEIGHT = 582;
    private Stage preloaderStage;
    private Scene preloaderScene;
    private ProgressBar bar;
    private Label progress;
	private Canvas preloaderCanvas;
	private MediaPlayer loadingPlayer;
	private MediaView loadingView;
    
    public void init() throws Exception{
    	Platform.runLater(() -> {
        	URL sound = getClass().getResource("/sounds/music/menu.wav");
    		loadingPlayer = new MediaPlayer(new Media(sound.toString()));
     		loadingPlayer.setAutoPlay(true);
     		loadingPlayer.setCycleCount(MediaPlayer.INDEFINITE);
     		
     		loadingView = new MediaView(loadingPlayer);
    		
    		progress = new Label("0%");
            bar = new ProgressBar(0);
            
            preloaderCanvas = new Canvas(500, 263);
            VBox root = new VBox(preloaderCanvas, bar, progress, loadingView);
            root.setAlignment(Pos.TOP_CENTER);
            root.setBackground(new Background(new BackgroundImage(new Image("images/backgrounds/preloader2.jpg"), null, null, null, new BackgroundSize(WIDTH, HEIGHT, false, false, true, true))));
            
            preloaderScene = new Scene(root, WIDTH, HEIGHT);
            preloaderScene.getStylesheets().add((getClass().getResource("/styles.css")).toExternalForm());
                        
            GraphicsContext prLdr = preloaderCanvas.getGraphicsContext2D();
                       
            Font fontType = Font.font("Helvetica", FontWeight.BOLD, 24);
            prLdr.setFont(fontType);
            prLdr.setFill(Color.GOLD);
            prLdr.setStroke(Color.BLACK);
            prLdr.setLineWidth(1);
    		            
            prLdr.fillText("Please wait. Loading...", 130, 255);
            prLdr.strokeText("Please wait. Loading...", 130, 255);
    	});
    }
    
	public void start(Stage primaryStage) throws Exception {
		preloaderStage = primaryStage;
		preloaderStage.setTitle("SP/ACE\\TEROIDS");
		preloaderStage.getIcons().add(new Image("images/logo/logo.png"));
		preloaderStage.setMaxWidth(WIDTH);
		preloaderStage.setMaxHeight(HEIGHT);
		preloaderStage.setScene(preloaderScene);
		preloaderStage.show();
	}

	public void handleApplicationNotification(PreloaderNotification info) {
		if (info instanceof ProgressNotification) {
			bar.setProgress(((ProgressNotification) info).getProgress() / 100);
            progress.setText(((ProgressNotification) info).getProgress() + "%");
        }
	}
	
	public void handleStateChangeNotification(StateChangeNotification info) {
		if(info.getType() == StateChangeNotification.Type.BEFORE_START) {
			loadingPlayer.stop();
			preloaderStage.hide();
		}
	}
}
