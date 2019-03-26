package spaceteroids.game_structure;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.javafx.application.LauncherImpl;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import spaceteroids.animated_sprites.ASBoss;
import spaceteroids.animated_sprites.ASEnemySpaceship;
import spaceteroids.animated_sprites.ASSpaceship;
import spaceteroids.animated_sprites.AnimatedSprite;
import spaceteroids.entity_generators.Asteroids;
import spaceteroids.entity_generators.BangBuffer;
import spaceteroids.entity_generators.Boosts;
import spaceteroids.sprite_shapes.Sprite;

/**
 * Main class of game "Spaceteroids". Here is described render and update logic of entities,
 * game menu with components, game animation with entities, event handlers for all components and
 * win or lose logic.
 * 
 * @author Stanislav Shergin
 *
 */
public class GameMain extends Application {
	// application window width and height
	private static final double WIDTH = 582;
	private static final double HEIGHT = 582;
	// version of the game
	private static final String gameVersion = "v3.3 - 03.2019";
	private double enemyEntityCount;
	private double checkTimerCap;
	private Stage gameStage;
	private SimpleTypeWrapper score;
	private SimpleTypeWrapper gameTime;
	private SimpleTypeWrapper checkTimer;
	private SimpleTypeWrapper bossStage;
	private List<AnimatedSprite> smallAsteroidList;
	private List<AnimatedSprite> mediumAsteroidList;
	private List<AnimatedSprite> largeAsteroidList;
	private List<ASEnemySpaceship> enemies;
	private List<ASBoss> boss;
	private List<String> input;
	private List<Sprite> laserTempList;
	private List<Sprite> heartBoostList;
	private List<AnimatedSprite> bangList;
	private List<AnimatedSprite> bangTempList;
	private List<AnimatedSprite> bigBangList;
	private List<AnimatedSprite> bigBangTempList;
	private List<AnimatedSprite> hitList;
	private List<AnimatedSprite> hitTempList;
	private ASSpaceship ship;
	private Sprite levelProgressBar;
	private Sprite shipProgressBar;
	private LoopableBackground space;
	private LoopableBackground fastStars;
	private LoopableBackground stars;
	private CollisionDetection gameCollision;
	@SuppressWarnings("unused")
	private BangBuffer bangBuf;
	private String consoleCommand;
	private ScoreBoard[] sb;
	private GameStatus gameStatus;
	// All group components are below
	private FlowPane labels;
	private Label lbActivityTime;
	private Label lbElapsedTime;
	private Label lbFps;
	private Label lbEEntities;
	private Label lbTests2;
	private Label lbScore;
	private Text menuText;
	private Text versionView;
	private VBox menuButtons;
	private Button newGameButton;
	private Button optionsButton;
	private Button scoreboardButton;
	private Button controlsButton;
	private Button aboutButton;
	private Button exitButton;
	private Button backButton;
	private MediaPlayer menuPlayer;
	private MediaView menuView;
	private MediaPlayer gamePlayer;
	private MediaView gameView;
	private MediaPlayer bossPlayer;
	private MediaPlayer losePlayer;
	private MediaPlayer winPlayer;
	
	/**
	 * Draws level progress bar with tiny spaceship.
	 * 
	 * @param gc - GraphicsContext where to draw level progress bar and spaceship marker.
	 * @param checkTimer - current level progress time.
	 * @param checkTimerCap - maximum level progress time.
	 */
	public void drawLevelProgress(GraphicsContext gc, double checkTimer, double checkTimerCap) {
		if(!(boss.isEmpty() || gameStatus.equals(GameStatus.AFTER_WIN))) {
			shipProgressBar.setPosition(79 + (checkTimer * (360 / checkTimerCap)), 521);
			levelProgressBar.render(gc);
			shipProgressBar.render(gc);
		}
	}
	
	/**
	 * Sets score label position.
	 * 
	 * @param posX - position by axis X.
	 * @param posY - position by axis Y.
	 */
	public void setLabelScorePosition(int posX, int posY) {
		lbScore.setLayoutX(posX);
		lbScore.setLayoutY(posY);
	}
	
	/**
	 * Resets level. All components are reinitialized to their default state.
	 */
	public void levelCleanup() {
		consoleCommand = "";
		setLabelScorePosition(380, 20);
		bossStage.setBooleanValue(false);
		gameCollision.cleanupCollision(0d, enemyEntityCount);
		Iterator<Sprite> laserIter = ship.getLaserList().iterator();
        while(laserIter.hasNext()){
        	laserIter.next();
        	laserIter.remove();
        }
        Iterator<Sprite> laserTempIter = laserTempList.iterator();
		while(laserTempIter.hasNext()){
			laserTempIter.next();
			laserTempIter.remove();
		}
        Iterator<AnimatedSprite> bangIter = bangList.iterator();
		while(bangIter.hasNext()){
			bangIter.next();
			bangIter.remove();
		}
		Iterator<AnimatedSprite> bangTempIter = bangTempList.iterator();
		while(bangTempIter.hasNext()){
			bangTempIter.next();
			bangTempIter.remove();
		}
		Iterator<AnimatedSprite> bigBangIter = bigBangList.iterator();
		while(bigBangIter.hasNext()){
			bigBangIter.next();
			bigBangIter.remove();
		}
		Iterator<AnimatedSprite> bigBangTempIter = bigBangTempList.iterator();
		while(bigBangTempIter.hasNext()){
			bigBangTempIter.next();
			bigBangTempIter.remove();
		}
		Iterator<AnimatedSprite> hitIter = hitList.iterator();
		while(hitIter.hasNext()){
			hitIter.next();
			hitIter.remove();
		}
		Iterator<AnimatedSprite> hitTempIter = hitTempList.iterator();
		while(hitTempIter.hasNext()){
			hitTempIter.next();
			hitTempIter.remove();
		}
		space.setPosition(-100, -100);
		stars.setPosition(-100, -100);
		fastStars.setPosition(-100, -100);
		ship.setPosition(240, 250);
		ship.setHealth(100);
		score.setIntValue(0);
		ASEnemySpaceship.regenerateEnemySpaceships(enemies, (int)(enemyEntityCount / 1.5), 50, WIDTH, 0, (2 * WIDTH), HEIGHT);
		ASBoss.regenerateBoss(boss, 1, 500, 850, 225);
		Boosts.regenerateBoosts(heartBoostList, 2, "heart", (4 * WIDTH), 0, (2 * WIDTH), HEIGHT);
		Asteroids.regenerateRandomAsteroids(smallAsteroidList, (int)(enemyEntityCount), "small", WIDTH, 0, (2 * WIDTH), HEIGHT);
		Asteroids.regenerateRandomAsteroids(mediumAsteroidList, (int)(enemyEntityCount / 3), "medium", (2 * WIDTH), 0, (2 * WIDTH), HEIGHT);
		Asteroids.regenerateRandomAsteroids(largeAsteroidList, (int)(enemyEntityCount / 5), "large", (4 * WIDTH), 0, (2 * WIDTH), HEIGHT);
	}
	
	/**
	 * Loads score board when it calls. If file "score.txt" is not created, 
	 * or corrupted or has size less than 80, this method creates new file with
	 * name "score.txt" with appropriate text filling.
	 */
	public void loadScoreBoard() {
		File txt = new File("score.txt");
		if(txt.length() <= 80) {
			for(int i = 0; i < sb.length; i++) {
				sb[i] = new ScoreBoard("--------", 0);
			}
			try(FileOutputStream f = new FileOutputStream(txt);
			    ObjectOutput s = new ObjectOutputStream(f)) {
				s.writeObject(sb);
			} catch(IOException ex){
			    ex.printStackTrace();
			}
		}
		try(FileInputStream in = new FileInputStream(txt);
		    ObjectInputStream s = new ObjectInputStream(in)) {
			sb = (ScoreBoard[]) s.readObject();
		} catch(Exception ex){
			try {
				txt.delete();
				txt.createNewFile();
				for(int i = 0; i < sb.length; i++) {
					sb[i] = new ScoreBoard("--------", 0);
				}
				try(FileOutputStream f = new FileOutputStream(txt);
				    ObjectOutput s = new ObjectOutputStream(f)) {
					s.writeObject(sb);
				} catch(IOException exin){
				    exin.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		    ex.printStackTrace();
		}
	}
	
	/**
	 * Writes score to file "score.txt" with linked name.
	 * 
	 * @param name - players name.
	 */
	public void saveScore(String name) {
		loadScoreBoard();
		if(name.equals("") || name.equals(" ")) {
			sb[10].setName(">LazyMan<");
		} else {
			sb[10].setName(name);
		}
		sb[10].setScore(score.getIntValue());
		for(int i = 0; i < sb.length; i++)// bubble sort
			for(int j = 0; j < sb.length - 1; j++) {
				if(sb[j].getIntScore() < sb[j+1].getIntScore()) {
					String nameSB = sb[j].getName();
					String scoreSB = sb[j].getScore();
					sb[j].setName(sb[j+1].getName());
					sb[j].setScore(sb[j+1].getScore());
					sb[j+1].setName(nameSB);
					sb[j+1].setScore(scoreSB);
				}
			}
		try(FileOutputStream f = new FileOutputStream("score.txt");
		    ObjectOutput s = new ObjectOutputStream(f)) {
			s.writeObject(sb);
		} catch(IOException ex){
		    ex.printStackTrace();
		}
	}
	
	/**
	 * Restarts the game after lose where it was called.
	 * 
	 * @param game - Group with components.
	 * @param gameAnimation - current AnimationTimer.
	 * @param before - MediaPlayer played before lose.
	 * @param after - MediaPlayer which should be played after lose.
	 */
	public void restartAfterLose(Group game, AnimationTimer gameAnimation, MediaPlayer before, MediaPlayer after) {
		try {
			gameAnimation.stop();
			gameCollision.stopCollision();
			before.stop();
			
			TextField nicknameField = new TextField();
			nicknameField.setAlignment(Pos.CENTER);
			nicknameField.setPromptText("Enter nickname");
			nicknameField.setPrefSize(150, 40);
			nicknameField.setLayoutX(200);
			nicknameField.setLayoutY(300);
			
			Button restartButton = new Button("RESTART");
			restartButton.setPrefSize(150, 40);
			restartButton.setLayoutX(200);
			restartButton.setLayoutY(350);
							
			nicknameField.setOnAction((ae) ->{
				saveScore(nicknameField.getText());
				game.getChildren().removeAll(nicknameField, restartButton);
				after.stop();
				gameView.setMediaPlayer(gamePlayer);
				levelCleanup();
				gameAnimation.start();
				gameCollision.startCollision();
				gamePlayer.play();
				gameStatus = GameStatus.START;
			});
			// sets limit to text field
			final int LIMIT = 10;
			nicknameField.lengthProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
	                if (newValue.intValue() > oldValue.intValue()) { // Check if the new character is greater than LIMIT
	                    if (nicknameField.getText().length() >= LIMIT) { // if it's 11th character then just setText to previous one
	                    	nicknameField.setText(nicknameField.getText().substring(0, LIMIT));
	                    }
	                }
	            }
			});
			
			restartButton.setOnMouseClicked((me) -> {
				saveScore(nicknameField.getText());
				game.getChildren().removeAll(nicknameField, restartButton);
				after.stop();
				gameView.setMediaPlayer(gamePlayer);
				levelCleanup();
				gameAnimation.start();
				gameCollision.startCollision();
				gamePlayer.play();
				gameStatus = GameStatus.START;
			});
		
			game.getChildren().addAll(nicknameField, restartButton);
			gameStatus = GameStatus.END_OF_GAME;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Restarts the game after win where it was called.
	 * 
	 * @param game - Group with components.
	 * @param gameAnimation - current AnimationTimer.
	 * @param gameMenu - menu AnimationTimer.
	 * @param current - current playing MediaPlayer.
	 * @param menuScene - menu Scene.
	 */
	public void restartAfterWin(Group game, AnimationTimer gameAnimation, AnimationTimer gameMenu, MediaPlayer current, Scene menuScene) {
		try {
			gameCollision.stopCollision();
			
			TextField nicknameField = new TextField();
			nicknameField.setAlignment(Pos.CENTER);
			nicknameField.setPromptText("Enter nickname");
			nicknameField.setPrefSize(150, 40);
			nicknameField.setLayoutX(200);
			nicknameField.setLayoutY(300);
			
			Button restartButton = new Button("NEW GAME");
			restartButton.setPrefSize(150, 40);
			restartButton.setLayoutX(200);
			restartButton.setLayoutY(350);
			
			Button menuButton = new Button("MAIN MENU");
			menuButton.setPrefSize(150, 40);
			menuButton.setLayoutX(200);
			menuButton.setLayoutY(400);
							
			nicknameField.setOnAction((ae) ->{
				saveScore(nicknameField.getText());
				game.getChildren().removeAll(nicknameField, restartButton, menuButton);
				current.stop();
				levelCleanup();
				gameView.setMediaPlayer(gamePlayer);
				gameCollision.startCollision();
				gamePlayer.play();
				gameStatus = GameStatus.START;
			});
			// sets limit to text field
			final int LIMIT = 10;
			nicknameField.lengthProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
	                if (newValue.intValue() > oldValue.intValue()) { // Check if the new character is greater than LIMIT
	                    if (nicknameField.getText().length() >= LIMIT) { // if it's 11th character then just setText to previous one
	                    	nicknameField.setText(nicknameField.getText().substring(0, LIMIT));
	                    }
	                }
	            }
			});
			
			restartButton.setOnMouseClicked((me) -> {
				saveScore(nicknameField.getText());
				game.getChildren().removeAll(nicknameField, restartButton, menuButton);
				current.stop();
				levelCleanup();
				gameView.setMediaPlayer(gamePlayer);
				gameCollision.startCollision();
				gamePlayer.play();
				gameStatus = GameStatus.START;
			});
			
			menuButton.setOnMouseClicked((me) -> {
				saveScore(nicknameField.getText());
				game.getChildren().removeAll(nicknameField, restartButton, menuButton);
				current.stop();
				gameAnimation.stop();
				levelCleanup();
				gameView.setMediaPlayer(menuPlayer);
				gameCollision.startCollision();
				menuPlayer.play();
				gameStage.setScene(menuScene);
				gameMenu.start();
				gameStatus = GameStatus.BEFORE_FIRST_START;
			});
		
			game.getChildren().addAll(nicknameField, restartButton, menuButton);
			gameStatus = GameStatus.AFTER_WIN;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
    public static void main(String[] args) {
		LauncherImpl.launchApplication(GameMain.class, GamePreloader.class, args);
	}
        
    public void init() throws Exception{
		int preloaderStagesCount = 0;
		int preloaderStages = 10;
		
		// set game status
		gameStatus = GameStatus.BEFORE_FIRST_START;
		
		// input storage
		input = new ArrayList<String>();
		
		// scoreboard and console command storage
		sb = new ScoreBoard[11];
		consoleCommand = "";
		
		// starting enemies count and level timer for events
		enemyEntityCount = 5;
		checkTimerCap = 217;
		
		// pane with labels
		labels = new FlowPane(Orientation.VERTICAL);
		labels.setColumnHalignment(HPos.LEFT);
		lbActivityTime = new Label();
		lbElapsedTime = new Label();
		lbFps = new Label("FPS: ");
		lbEEntities = new Label("Label for tests");
		lbTests2 = new Label("Waiting commands");
		lbScore = new Label("Score: 0");
		lbScore.setId("score");
		setLabelScorePosition(380, 20);
		LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification((100 * ++preloaderStagesCount) / preloaderStages));
		
		menuText = new Text(30, 80, "SP/ACE\\TEROIDS");
		menuText.setId("gamename");
		
		versionView = new Text(WIDTH - 125, HEIGHT - 50, gameVersion);
		versionView.setId("gametext");
		
		// pane with control buttons
		menuButtons = new VBox();
		menuButtons.setSpacing(15);
		menuButtons.setPadding(new Insets(120, 0, 0, 60));
		newGameButton = new Button("NEW GAME");
		newGameButton.setPrefSize(150, 40);
		optionsButton = new Button("OPTIONS");
		optionsButton.setPrefSize(150, 40);
		scoreboardButton = new Button("SCOREBOARD");
		scoreboardButton.setPrefSize(150, 40);
		controlsButton = new Button("CONTROLS");
		controlsButton.setPrefSize(150, 40);
		aboutButton = new Button("ABOUT");
		aboutButton.setPrefSize(150, 40);
		exitButton = new Button("EXIT");
		exitButton.setPrefSize(150, 40);
		backButton = new Button("BACK");
		backButton.setPrefSize(150, 40);
		backButton.setLayoutX(60);
		backButton.setLayoutY(120);
				
		menuButtons.getChildren().addAll(newGameButton, optionsButton, scoreboardButton, controlsButton, aboutButton, exitButton);
		LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification((100 * ++preloaderStagesCount) / preloaderStages));
		
		// entities initialization
		smallAsteroidList = Asteroids.generateRandomAsteroids((int)(enemyEntityCount), "small", WIDTH, 0, (2 * WIDTH), HEIGHT);
		LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification((100 * ++preloaderStagesCount) / preloaderStages));
		
		mediumAsteroidList = Asteroids.generateRandomAsteroids((int)(enemyEntityCount / 3), "medium", (2 * WIDTH), 0, (2 * WIDTH), HEIGHT);
		LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification((100 * ++preloaderStagesCount) / preloaderStages));
		
		largeAsteroidList = Asteroids.generateRandomAsteroids((int)(enemyEntityCount / 5), "large", (4 * WIDTH), 0, (2 * WIDTH), HEIGHT);
		LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification((100 * ++preloaderStagesCount) / preloaderStages));
        
		enemies = ASEnemySpaceship.generateEnemySpaceships((int)(enemyEntityCount / 1.5), 50, WIDTH, 0, (2 * WIDTH), HEIGHT);
		boss = ASBoss.generateBoss(1, 500, 850, 225);
		heartBoostList = Boosts.generateBoosts(2, "heart", (4 * WIDTH), 0, (2 * WIDTH), HEIGHT);
		
		// loading buffer with explosions
		bangList = new ArrayList<AnimatedSprite>();
		bangTempList = new ArrayList<AnimatedSprite>();
		bigBangList = new ArrayList<AnimatedSprite>();
		bigBangTempList = new ArrayList<AnimatedSprite>();
		hitList = new ArrayList<AnimatedSprite>();
		hitTempList = new ArrayList<AnimatedSprite>();
		laserTempList = new ArrayList<Sprite>();
		bangBuf = new BangBuffer(bangTempList, bigBangTempList, laserTempList, hitTempList);
		LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification((100 * ++preloaderStagesCount) / preloaderStages));
		
        // create spaceship
		ship = new ASSpaceship(100);
		// initialization of level progress bar
		levelProgressBar = new Sprite("images/progressbar/pb2.png", 100, 527, 0, 0);
	    shipProgressBar = new Sprite("images/ship/fff1-min.png", 42, 18, true, true, 79, 521, 0, 0);
        LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification((100 * ++preloaderStagesCount) / preloaderStages));
        
        score = new SimpleTypeWrapper(0);
     	gameTime = new SimpleTypeWrapper();
     	checkTimer = new SimpleTypeWrapper(0d);
     	bossStage = new SimpleTypeWrapper(false);
     	LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification((100 * ++preloaderStagesCount) / preloaderStages));
     	
     	URL sound = getClass().getResource("/sounds/music/Beyond the Stars (Ambient).mp3");
     	menuPlayer = new MediaPlayer(new Media(sound.toString()));
		menuPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		menuView = new MediaView(menuPlayer);
		
		sound = getClass().getResource("/sounds/music/BossTheme.mp3");
		bossPlayer = new MediaPlayer(new Media(sound.toString()));
		bossPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		
		sound = getClass().getResource("/sounds/music/No Hope.mp3");
		losePlayer = new MediaPlayer(new Media(sound.toString()));
		
		sound = getClass().getResource("/sounds/music/wind.mp3");
		winPlayer = new MediaPlayer(new Media(sound.toString()));
		
		sound = getClass().getResource("/sounds/music/Out_Of_Time.mp3");
		gamePlayer = new MediaPlayer(new Media(sound.toString()));
		gamePlayer.setCycleCount(MediaPlayer.INDEFINITE);
		gameView = new MediaView(gamePlayer);
		LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification((100 * ++preloaderStagesCount) / preloaderStages));
		
		space = new LoopableBackground("images/backgrounds/space4.png", -100, -100, -2, 0);
		stars = new LoopableBackground("images/backgrounds/22.png", -100, -100, -4, 0);
		fastStars = new LoopableBackground("images/backgrounds/44.png", -100, -100, -6, 0);
		gameCollision = new CollisionDetection();
		LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification((100 * ++preloaderStagesCount) / preloaderStages));
    }
    
    public void start(Stage primaryStage) throws Exception{
		gameStage = primaryStage;
					
		// settings of window
		gameStage.setTitle("SP/ACE\\TEROIDS");
		gameStage.getIcons().add(new Image("images/logo/logo.png"));
		gameStage.setMaxWidth(WIDTH);
		gameStage.setMaxHeight(HEIGHT);
		
		// menu group
		Group menu = new Group();
		Scene menuScene = new Scene(menu);
		menuScene.getStylesheets().add((getClass().getResource("/styles.css")).toExternalForm());
		Canvas menuCanvas = new Canvas(WIDTH, HEIGHT);
		
		// add all to menu node
		menu.getChildren().addAll(menuCanvas, menuButtons, menuText, menuView, versionView);
		
		// game group
		Group game = new Group();
		Scene gameScene = new Scene(game);
		gameScene.getStylesheets().add((getClass().getResource("/styles.css")).toExternalForm());
		Canvas gameCanvas = new Canvas(WIDTH, HEIGHT);
		
		// add all to game (root) node
		game.getChildren().addAll(gameCanvas, gameView, lbScore);
		
		menuPlayer.play();
		
		gameStage.setScene(menuScene);
		
		GraphicsContext gcMenu = menuCanvas.getGraphicsContext2D();
		GraphicsContext gcGame = gameCanvas.getGraphicsContext2D();
					
		gcGame.setFill(Color.YELLOW);
		gcGame.setStroke(Color.FIREBRICK);
		gcGame.setLineWidth(2);
		gcMenu.setFill(Color.YELLOW);
		gcMenu.setStroke(Color.FIREBRICK);
		gcMenu.setLineWidth(2);
							
		// application start timestamp
     	final double startNanoTime = System.nanoTime();
     	SimpleTypeWrapper collisionStart = new SimpleTypeWrapper(true);
     	
     	// game menu
     	AnimationTimer gameMenu = new AnimationTimer() {
			
			public void handle(long currentNanoTime) {
				double time = (currentNanoTime - startNanoTime) / 1000000000.0;
							
				// render sprites
				space.render(gcMenu);
				stars.render(gcMenu);
				fastStars.render(gcMenu);
				
				if(gameStatus.equals(GameStatus.BEFORE_FIRST_START)) {
					space.update(0.06);
					stars.update(0.06);
					fastStars.update(0.06);
				}
				
				for(AnimatedSprite tempAsteroid: smallAsteroidList) {
					tempAsteroid.render(time, gcMenu);
				}
				for(AnimatedSprite tempAsteroid: mediumAsteroidList) {
					tempAsteroid.render(time, gcMenu);
				}
				for(AnimatedSprite tempAsteroid: largeAsteroidList) {
					tempAsteroid.render(time, gcMenu);
				}
				for(Sprite tempBoost: heartBoostList) {
					tempBoost.render(gcMenu);
				}
				for(ASBoss bossTemp: boss) {
					bossTemp.render(time, gcMenu);
				}
				for(AnimatedSprite tempBang: bangList) {
					tempBang.getFrame((gameTime.getDoubleValue() + time) - tempBang.getTime()).render(gcMenu);
				}
				for(AnimatedSprite tempBang: bigBangList) {
					tempBang.getFrame((gameTime.getDoubleValue() + time) - tempBang.getTime()).render(gcMenu);
				}
				for(ASEnemySpaceship tempEnemy: enemies) {
					tempEnemy.render(time, gcMenu);
				}
				for(AnimatedSprite tempHit: hitList) {
					tempHit.getFrame((gameTime.getDoubleValue() + time) - tempHit.getTime()).render(gcMenu);
				}
				ship.render(time, gcMenu);
			}
		};
		// menu starts first
		gameMenu.start();
				
		// game animation
		AnimationTimer gameAnimation = new AnimationTimer() {
			double framesPerSecond = 0;
			long lastNanoTime = System.nanoTime();
			
			/**
			 * Shows frames per second.
			 * 
			 * @param currentTime - time of each frame.
			 */
			public void frameRate(long currentTime) {
				double elapsedTime = (currentTime - lastNanoTime)/1000000000.0;
				lbElapsedTime.setText("Elapsed time: " + elapsedTime);
				
				++framesPerSecond;					
				
				if(elapsedTime > 1) {
					lastNanoTime = currentTime;
					lbFps.setText("FPS: " + framesPerSecond);
					framesPerSecond = 0;
				}
			}
			
			public void handle(long currentNanoTime) {
				double time = (currentNanoTime - startNanoTime)/1000000000d;
				gameTime.setDoubleValue(time);
				frameRate(currentNanoTime);
				
				// labels changes
				if(laserTempList.size() == 0) {
					lbTests2.setText("Reloading!");
				} else if(input.contains("SPACE")) {
					lbTests2.setText("Firing!");
				} else {
					lbTests2.setText("Waiting commands!");
				}
				lbActivityTime.setText("Activity time: " + time);
				lbEEntities.setText("Enemy entities: SA " + smallAsteroidList.size() +
						" MA " + mediumAsteroidList.size() + " LA " + largeAsteroidList.size() + " E " + enemies.size());
				
				// collision start
				if(collisionStart.getBooleanValue()) {
					gameCollision.setParams(WIDTH, HEIGHT, enemyEntityCount, score, checkTimer, bossStage, smallAsteroidList, mediumAsteroidList, largeAsteroidList, heartBoostList, 
							ship.getLaserList(), bangList, bangTempList, bigBangList, bigBangTempList, hitList, hitTempList, startNanoTime, ship, boss, enemies);
					gameCollision.start();
					collisionStart.setBooleanValue(false);
				}		
								
				// ship controls for environment effects
				if(!(gameStatus.equals(GameStatus.END_OF_GAME))) {
					ship.setVelocity(0, 0);
				}
				if(!(bossStage.getBooleanValue())) {
					space.setVelocity(-2, 0);
					stars.setVelocity(-4, 0);
					fastStars.setVelocity(-6, 0);
				} else {
					space.setVelocity(0, 0);
					stars.setVelocity(0, 0);
					fastStars.setVelocity(0, 0);
				}
				Asteroids.setDefaultAsteroidVelocity(smallAsteroidList, time);
				Asteroids.setDefaultAsteroidVelocity(mediumAsteroidList, time);
				Asteroids.setDefaultAsteroidVelocity(largeAsteroidList, time);
				Boosts.setBoostsVelocity(heartBoostList, -33, 0);
				ASEnemySpaceship.setDefaultEnemyVelocity(enemies);
				for(Sprite tempLaser: ship.getLaserList()) {
					tempLaser.setVelocity(150, 0);
				}
				if(!(bossStage.getBooleanValue())) {
					for(AnimatedSprite tempBang: bangList) {
						tempBang.setVelocity(-66, 0);
					}
					for(AnimatedSprite tempBang: bigBangList) {
						tempBang.setVelocity(-66, 0);
					}
					for(AnimatedSprite tempHit: hitList) {
						tempHit.setVelocity(20, 0);
					}
				}

				// ship controls
				if((input.contains("D") || input.contains("RIGHT")) && !(gameStatus.equals(GameStatus.END_OF_GAME) || gameStatus.equals(GameStatus.AFTER_WIN))) {
					ship.addVelocity(150, 0);
					if(!(bossStage.getBooleanValue())) {
						space.addVelocity(-2, 0);
						stars.addVelocity(-4, 0);
						fastStars.addVelocity(-6, 0);
						for(AnimatedSprite asteroidTemp: smallAsteroidList) {
							asteroidTemp.addVelocity(-6, 0);
						}
						for(AnimatedSprite asteroidTemp: mediumAsteroidList) {
							asteroidTemp.addVelocity(-6, 0);
						}
						for(AnimatedSprite asteroidTemp: largeAsteroidList) {
							asteroidTemp.addVelocity(-6, 0);
						}
						for(Sprite boostTemp: heartBoostList) {
							boostTemp.addVelocity(-6, 0);
						}
						for(AnimatedSprite tempBang: bangList) {
							tempBang.addVelocity(-6, 0);
						}
						for(AnimatedSprite tempBang: bigBangList) {
							tempBang.addVelocity(-6, 0);
						}
						for(AnimatedSprite tempHit: hitList) {
							tempHit.addVelocity(6, 0);
						}
						for(ASEnemySpaceship tempEnemy: enemies) {
							tempEnemy.addVelocity(-6, 0);
							for(Sprite tempLaser: tempEnemy.getLaserList()) {
								tempLaser.addVelocity(-6, 0);
							}
						}
						for(Sprite tempLaser: ship.getLaserList()) {
							tempLaser.addVelocity(-6, 0);
						}
					}
				}
				if((input.contains("A") || input.contains("LEFT")) && !(gameStatus.equals(GameStatus.END_OF_GAME) || gameStatus.equals(GameStatus.AFTER_WIN))) {
					ship.addVelocity(-150, 0);
					if(!(bossStage.getBooleanValue())) {
						space.addVelocity(4, 0);
						stars.addVelocity(8, 0);
						fastStars.addVelocity(12, 0);
						for(AnimatedSprite asteroidTemp: smallAsteroidList) {
							asteroidTemp.addVelocity(12, 0);
						}
						for(AnimatedSprite asteroidTemp: mediumAsteroidList) {
							asteroidTemp.addVelocity(12, 0);
						}
						for(AnimatedSprite asteroidTemp: largeAsteroidList) {
							asteroidTemp.addVelocity(12, 0);
						}
						for(Sprite boostTemp: heartBoostList) {
							boostTemp.addVelocity(12, 0);
						}
						for(AnimatedSprite tempBang: bangList) {
							tempBang.addVelocity(12, 0);
						}
						for(AnimatedSprite tempBang: bigBangList) {
							tempBang.addVelocity(12, 0);
						}
						for(AnimatedSprite tempHit: hitList) {
							tempHit.addVelocity(-12, 0);
						}
						for(ASEnemySpaceship tempEnemy: enemies) {
							tempEnemy.addVelocity(12, 0);
							for(Sprite tempLaser: tempEnemy.getLaserList()) {
								tempLaser.addVelocity(12, 0);
							}
						}
						for(Sprite tempLaser: ship.getLaserList()) {
							tempLaser.addVelocity(12, 0);
						}
					}
				}
				if((input.contains("W") || input.contains("UP")) && !(gameStatus.equals(GameStatus.END_OF_GAME) || gameStatus.equals(GameStatus.AFTER_WIN))) {
					ship.addVelocity(0, -150);
					if(!(bossStage.getBooleanValue())) {
						space.addVelocity(0, 3);
						stars.addVelocity(0, 6);
						fastStars.addVelocity(0, 9);
						for(AnimatedSprite asteroidTemp: smallAsteroidList) {
							asteroidTemp.addVelocity(0, 9);
						}
						for(AnimatedSprite asteroidTemp: mediumAsteroidList) {
							asteroidTemp.addVelocity(0, 9);
						}
						for(AnimatedSprite asteroidTemp: largeAsteroidList) {
							asteroidTemp.addVelocity(0, 9);
						}
						for(Sprite boostTemp: heartBoostList) {
							boostTemp.addVelocity(0, 9);
						}
						for(AnimatedSprite tempBang: bangList) {
							tempBang.addVelocity(0, 9);
						}
						for(AnimatedSprite tempBang: bigBangList) {
							tempBang.addVelocity(0, 9);
						}
						for(AnimatedSprite tempHit: hitList) {
							tempHit.addVelocity(0, 9);
						}
						for(ASEnemySpaceship tempEnemy: enemies) {
							tempEnemy.addVelocity(0, 9);
							for(Sprite tempLaser: tempEnemy.getLaserList()) {
								tempLaser.addVelocity(0, 9);
							}
						}
						for(Sprite tempLaser: ship.getLaserList()) {
							tempLaser.addVelocity(0, 9);
						}
					}
				}
				if((input.contains("S") || input.contains("DOWN")) && !(gameStatus.equals(GameStatus.END_OF_GAME) || gameStatus.equals(GameStatus.AFTER_WIN))) {
					ship.addVelocity(0, 150);
					if(!(bossStage.getBooleanValue())) {
						space.addVelocity(0, -3);
						stars.addVelocity(0, -6);
						fastStars.addVelocity(0, -9);
						for(AnimatedSprite asteroidTemp: smallAsteroidList) {
							asteroidTemp.addVelocity(0, -9);
						}
						for(AnimatedSprite asteroidTemp: mediumAsteroidList) {
							asteroidTemp.addVelocity(0, -9);
						}
						for(AnimatedSprite asteroidTemp: largeAsteroidList) {
							asteroidTemp.addVelocity(0, -9);
						}
						for(Sprite boostTemp: heartBoostList) {
							boostTemp.addVelocity(0, -9);
						}
						for(AnimatedSprite tempBang: bangList) {
							tempBang.addVelocity(0, -9);
						}
						for(AnimatedSprite tempBang: bigBangList) {
							tempBang.addVelocity(0, -9);
						}
						for(AnimatedSprite tempHit: hitList) {
							tempHit.addVelocity(0, -9);
						}
						for(ASEnemySpaceship tempEnemy: enemies) {
							tempEnemy.addVelocity(0, -9);
							for(Sprite tempLaser: tempEnemy.getLaserList()) {
								tempLaser.addVelocity(0, -9);
							}
						}
						for(Sprite tempLaser: ship.getLaserList()) {
							tempLaser.addVelocity(0, -9);
						}
					}
				}
				// check bounds of window sides and corners for ship
				if(!(gameStatus.equals(GameStatus.END_OF_GAME) || gameStatus.equals(GameStatus.AFTER_WIN))) {
			        // window border for ship (+ space, enemies and asteroids reactions)
					if(ship.getFrame(time).getPositionX() >= (WIDTH - 80)) {
						ship.addVelocity(-150, 0);
						if(!(bossStage.getBooleanValue())) {
							space.addVelocity(2, 0);
							stars.addVelocity(4, 0);
							fastStars.addVelocity(6, 0);
							Asteroids.setDefaultAsteroidVelocity(smallAsteroidList, time);
							Asteroids.setDefaultAsteroidVelocity(mediumAsteroidList, time);
							Asteroids.setDefaultAsteroidVelocity(largeAsteroidList, time);
							Boosts.setBoostsVelocity(heartBoostList, -33, 0);
							for(AnimatedSprite tempBang: bangList) {
								tempBang.setVelocity(-66, 0);
							}
							for(AnimatedSprite tempBang: bigBangList) {
								tempBang.setVelocity(-66, 0);
							}
							for(AnimatedSprite tempHit: hitList) {
								tempHit.setVelocity(20, 0);
							}
							ASEnemySpaceship.setDefaultEnemyVelocity(enemies);
							for(Sprite tempLaser: ship.getLaserList()) {
								tempLaser.setVelocity(150, 0);
							}
						}
					}
					else if(ship.getFrame(time).getPositionX() <= -15) {
						ship.addVelocity(150, 0);
						if(!(bossStage.getBooleanValue())) {
							space.addVelocity(-4, 0);
							stars.addVelocity(-8, 0);
							fastStars.addVelocity(-12, 0);
							Asteroids.setDefaultAsteroidVelocity(smallAsteroidList, time);
							Asteroids.setDefaultAsteroidVelocity(mediumAsteroidList, time);
							Asteroids.setDefaultAsteroidVelocity(largeAsteroidList, time);
							Boosts.setBoostsVelocity(heartBoostList, -33, 0);
							for(AnimatedSprite tempBang: bangList) {
								tempBang.setVelocity(-66, 0);
							}
							for(AnimatedSprite tempBang: bigBangList) {
								tempBang.setVelocity(-66, 0);
							}
							for(AnimatedSprite tempHit: hitList) {
								tempHit.setVelocity(20, 0);
							}
							ASEnemySpaceship.setDefaultEnemyVelocity(enemies);
							for(Sprite tempLaser: ship.getLaserList()) {
								tempLaser.setVelocity(150, 0);
							}
						}
					}
					else if(ship.getFrame(time).getPositionY() <= 0) {
						ship.addVelocity(0, 150);
						if(!(bossStage.getBooleanValue())) {
							space.addVelocity(0, -3);
							stars.addVelocity(0, -6);
							fastStars.addVelocity(0, -9);
							for(AnimatedSprite asteroidTemp: smallAsteroidList) {
								asteroidTemp.addVelocity(0, -9);
							}
							for(AnimatedSprite asteroidTemp: mediumAsteroidList) {
								asteroidTemp.addVelocity(0, -9);
							}
							for(AnimatedSprite asteroidTemp: largeAsteroidList) {
								asteroidTemp.addVelocity(0, -9);
							}
							for(Sprite boostTemp: heartBoostList) {
								boostTemp.addVelocity(0, -9);
							}
							for(AnimatedSprite tempBang: bangList) {
								tempBang.addVelocity(0, -9);
							}
							for(AnimatedSprite tempBang: bigBangList) {
								tempBang.addVelocity(0, -9);
							}
							for(AnimatedSprite tempHit: hitList) {
								tempHit.addVelocity(0, -9);
							}
							for(ASEnemySpaceship tempEnemy: enemies) {
								tempEnemy.addVelocity(0, -9);
								for(Sprite tempLaser: tempEnemy.getLaserList()) {
									tempLaser.addVelocity(0, -9);
								}
							}
							for(Sprite tempLaser: ship.getLaserList()) {
								tempLaser.addVelocity(0, -9);
							}
						}
					}
					else if(ship.getFrame(time).getPositionY() >= (HEIGHT - 68)) {
						ship.addVelocity(0, -150);
						if(!(bossStage.getBooleanValue())) {
							space.addVelocity(0, 3);
							stars.addVelocity(0, 6);
							fastStars.addVelocity(0, 9);
							for(AnimatedSprite asteroidTemp: smallAsteroidList) {
								asteroidTemp.addVelocity(0, 9);
							}
							for(AnimatedSprite asteroidTemp: mediumAsteroidList) {
								asteroidTemp.addVelocity(0, 9);
							}
							for(AnimatedSprite asteroidTemp: largeAsteroidList) {
								asteroidTemp.addVelocity(0, 9);
							}
							for(Sprite boostTemp: heartBoostList) {
								boostTemp.addVelocity(0, 9);
							}
							for(AnimatedSprite tempBang: bangList) {
								tempBang.addVelocity(0, 9);
							}
							for(AnimatedSprite tempBang: bigBangList) {
								tempBang.addVelocity(0, 9);
							}
							for(AnimatedSprite tempHit: hitList) {
								tempHit.addVelocity(0, 9);
							}
							for(ASEnemySpaceship tempEnemy: enemies) {
								tempEnemy.addVelocity(0, 9);
								for(Sprite tempLaser: tempEnemy.getLaserList()) {
									tempLaser.addVelocity(0, 9);
								}
							}
							for(Sprite tempLaser: ship.getLaserList()) {
								tempLaser.addVelocity(0, 9);
							}
						}
					}
					// window corners check for ship 
					if(ship.getFrame(time).getPositionY() >= (HEIGHT - 68) && ship.getFrame(time).getPositionX() >= (WIDTH - 80)) { // right bottom
						ship.addVelocity(-150, -150);
						if(!(bossStage.getBooleanValue())) {
							space.addVelocity(2, 3);
							stars.addVelocity(4, 6);
							fastStars.addVelocity(6, 9);
							for(AnimatedSprite asteroidTemp: smallAsteroidList) {
								asteroidTemp.addVelocity(6, 0);
							}
							for(AnimatedSprite asteroidTemp: mediumAsteroidList) {
								asteroidTemp.addVelocity(6, 0);
							}
							for(AnimatedSprite asteroidTemp: largeAsteroidList) {
								asteroidTemp.addVelocity(6, 0);
							}
							for(Sprite boostTemp: heartBoostList) {
								boostTemp.addVelocity(6, 0);
							}
							for(AnimatedSprite tempBang: bangList) {
								tempBang.addVelocity(6, 0);
							}
							for(AnimatedSprite tempBang: bigBangList) {
								tempBang.addVelocity(6, 0);
							}
							for(AnimatedSprite tempHit: hitList) {
								tempHit.addVelocity(-6, 0);
							}
							for(ASEnemySpaceship tempEnemy: enemies) {
								tempEnemy.addVelocity(6, 0);
								for(Sprite tempLaser: tempEnemy.getLaserList()) {
									tempLaser.addVelocity(6, 0);
								}
							}
							for(Sprite tempLaser: ship.getLaserList()) {
								tempLaser.addVelocity(6, 0);
							}
						}
					} else if(ship.getFrame(time).getPositionY() >= (HEIGHT - 68) && ship.getFrame(time).getPositionX() <= -15) { // left bottom
						ship.addVelocity(150, -150);
						if(!(bossStage.getBooleanValue())) {
							space.addVelocity(-4, 3);
							stars.addVelocity(-8, 6);
							fastStars.addVelocity(-12, 9);
							for(AnimatedSprite asteroidTemp: smallAsteroidList) {
								asteroidTemp.addVelocity(-12, 0);
							}
							for(AnimatedSprite asteroidTemp: mediumAsteroidList) {
								asteroidTemp.addVelocity(-12, 0);
							}
							for(AnimatedSprite asteroidTemp: largeAsteroidList) {
								asteroidTemp.addVelocity(-12, 0);
							}
							for(Sprite boostTemp: heartBoostList) {
								boostTemp.addVelocity(-12, 0);
							}
							for(AnimatedSprite tempBang: bangList) {
								tempBang.addVelocity(-12, 0);
							}
							for(AnimatedSprite tempBang: bigBangList) {
								tempBang.addVelocity(-12, 0);
							}
							for(AnimatedSprite tempHit: hitList) {
								tempHit.addVelocity(12, 0);
							}
							for(ASEnemySpaceship tempEnemy: enemies) {
								tempEnemy.addVelocity(-12, 0);
								for(Sprite tempLaser: tempEnemy.getLaserList()) {
									tempLaser.addVelocity(-12, 0);
								}
							}
							for(Sprite tempLaser: ship.getLaserList()) {
								tempLaser.addVelocity(-12, 0);
							}
						}
					} else if(ship.getFrame(time).getPositionX() <= -15 && ship.getFrame(time).getPositionY() <= 0) { // left top
						ship.addVelocity(150, 150);
						if(!(bossStage.getBooleanValue())) {
							space.addVelocity(-4, -3);
							stars.addVelocity(-8, -6);
							fastStars.addVelocity(-12, -9);
							for(AnimatedSprite asteroidTemp: smallAsteroidList) {
								asteroidTemp.addVelocity(-12, 0);
							}
							for(AnimatedSprite asteroidTemp: mediumAsteroidList) {
								asteroidTemp.addVelocity(-12, 0);
							}
							for(AnimatedSprite asteroidTemp: largeAsteroidList) {
								asteroidTemp.addVelocity(-12, 0);
							}
							for(Sprite boostTemp: heartBoostList) {
								boostTemp.addVelocity(-12, 0);
							}
							for(AnimatedSprite tempBang: bangList) {
								tempBang.addVelocity(-12, 0);
							}
							for(AnimatedSprite tempBang: bigBangList) {
								tempBang.addVelocity(-12, 0);
							}
							for(AnimatedSprite tempHit: hitList) {
								tempHit.addVelocity(12, 0);
							}
							for(ASEnemySpaceship tempEnemy: enemies) {
								tempEnemy.addVelocity(-12, 0);
								for(Sprite tempLaser: tempEnemy.getLaserList()) {
									tempLaser.addVelocity(-12, 0);
								}
							}
							for(Sprite tempLaser: ship.getLaserList()) {
								tempLaser.addVelocity(-12, 0);
							}
						}
					} else if(ship.getFrame(time).getPositionX() >= (WIDTH - 80) && ship.getFrame(time).getPositionY() <= 0) { // right top
						ship.addVelocity(-150, 150);
						if(!(bossStage.getBooleanValue())) {
							space.addVelocity(2, -3);
							stars.addVelocity(4, -6);
							fastStars.addVelocity(6, -9);
							for(AnimatedSprite asteroidTemp: smallAsteroidList) {
								asteroidTemp.addVelocity(6, 0);
							}
							for(AnimatedSprite asteroidTemp: mediumAsteroidList) {
								asteroidTemp.addVelocity(6, 0);
							}
							for(AnimatedSprite asteroidTemp: largeAsteroidList) {
								asteroidTemp.addVelocity(6, 0);
							}
							for(Sprite boostTemp: heartBoostList) {
								boostTemp.addVelocity(6, 0);
							}
							for(AnimatedSprite tempBang: bangList) {
								tempBang.addVelocity(6, 0);
							}
							for(AnimatedSprite tempBang: bigBangList) {
								tempBang.addVelocity(6, 0);
							}
							for(AnimatedSprite tempHit: hitList) {
								tempHit.addVelocity(-6, 0);
							}
							for(ASEnemySpaceship tempEnemy: enemies) {
								tempEnemy.addVelocity(6, 0);
								for(Sprite tempLaser: tempEnemy.getLaserList()) {
									tempLaser.addVelocity(6, 0);
								}
							}
							for(Sprite tempLaser: ship.getLaserList()) {
								tempLaser.addVelocity(6, 0);
							}
						}
					}
				}
				// timer for game events
				if(!(bossStage.getBooleanValue()) && !(gameStatus.equals(GameStatus.END_OF_GAME) || gameStatus.equals(GameStatus.AFTER_WIN))) {
					checkTimer.setDoubleValue(checkTimer.getDoubleValue() + 0.02);
				}
				if(checkTimer.getDoubleValue() >= checkTimerCap && checkTimer.getDoubleValue() <= (checkTimerCap + 0.02)) {
					checkTimer.setDoubleValue(checkTimer.getDoubleValue() + 0.04);
					bossStage.setBooleanValue(true);
				}
		        // render and update sprites
				space.update(0.06);
				space.render(gcGame);
				stars.update(0.06);
				stars.render(gcGame);
				fastStars.update(0.06);
				fastStars.render(gcGame);
												
				for(AnimatedSprite tempAsteroid: smallAsteroidList) {
					tempAsteroid.update(0.06);
					if(tempAsteroid.getPositionX() < (WIDTH + 50)) {
						tempAsteroid.render(time, gcGame);
					}
				}			
				for(AnimatedSprite tempAsteroid: mediumAsteroidList) {
					tempAsteroid.update(0.06);
					if(tempAsteroid.getPositionX() < (WIDTH + 75)) {
						tempAsteroid.render(time, gcGame);
					}
				}
				for(AnimatedSprite tempAsteroid: largeAsteroidList) {
					tempAsteroid.update(0.06);
					if(tempAsteroid.getPositionX() < (WIDTH + 150)) {
						tempAsteroid.render(time, gcGame);
					}
				}
				for(Sprite tempBoost: heartBoostList) {
					tempBoost.update(0.06);
					if(tempBoost.getPositionX() < (WIDTH + 50)) {
						tempBoost.render(gcGame);
					}
				}
				// start boss when boss stage began
				if(bossStage.getBooleanValue()) {
					gamePlayer.stop();
					gameView.setMediaPlayer(bossPlayer);
					bossPlayer.play();
					for(ASBoss bossTemp: boss) {
						bossTemp.updateAndRender(time, gcGame, enemies, smallAsteroidList);
					}
				}
				for(AnimatedSprite tempBang: bangList) {
					tempBang.update(0.06);
					tempBang.getFrameEachOnce(time - tempBang.getTime()).render(gcGame);
				}
				for(AnimatedSprite tempBigBang: bigBangList) {
					tempBigBang.update(0.06);
					tempBigBang.getFrameEachOnce(time - tempBigBang.getTime()).render(gcGame);
				}
				for(ASEnemySpaceship tempEnemy: enemies) {
					tempEnemy.updateAndRender(time, gcGame, WIDTH);
				}
				for(AnimatedSprite tempHit: hitList) {
					tempHit.update(0.06);
					tempHit.getFrameEachOnce(time - tempHit.getTime()).render(gcGame);
				}
				// for render animated sprites taking current sprite frame in animation			
				ship.updateAndRender(time, gcGame, laserTempList);
				
				drawLevelProgress(gcGame, checkTimer.getDoubleValue(), checkTimerCap);
				
				// "end of game" reaction
				if(boss.isEmpty() && !(gameStatus.equals(GameStatus.END_OF_GAME) || gameStatus.equals(GameStatus.AFTER_WIN))) { // show winner
					bossPlayer.stop();
					gameView.setMediaPlayer(winPlayer);
					winPlayer.play();
					lbScore.setText("     You won!\nYour score: " + score.getIntValue());
					setLabelScorePosition(186, 226);
					bossStage.setBooleanValue(false);
					gameStatus = GameStatus.END_OF_GAME;
				} else if(ship.getHealth() <= 0) { // detect lose
					if(bossStage.getBooleanValue()) {
						restartAfterLose(game, this, bossPlayer, losePlayer);
					} else {
						restartAfterLose(game, this, gamePlayer, losePlayer);
					}
					gameView.setMediaPlayer(losePlayer);
					losePlayer.play();
					lbScore.setText("     You lose!\nYour score: " + score.getIntValue());
					setLabelScorePosition(186, 226);
				} else if(!(gameStatus.equals(GameStatus.END_OF_GAME) || gameStatus.equals(GameStatus.AFTER_WIN))) {
					lbScore.setText("Score: " + score.getIntValue()); // show score
				} else {
					if(ship.getPositionX() > (WIDTH + 25) && !(gameStatus.equals(GameStatus.AFTER_WIN))) {
						restartAfterWin(game, this, gameMenu, winPlayer, menuScene);
					}
					if((int)ship.getPositionX() <= (WIDTH + 10)) {
						ship.setVelocity(20, 0);				
						if((int)ship.getPositionY() != 251){
							if((int)ship.getPositionY() > 251) {
								ship.setVelocity(0, -20);
							} else {
								ship.setVelocity(0, 20);
							}
						}
					}
				}
			}
		};
		
		// handle keys events
		gameScene.setOnKeyPressed((ke) -> {
			String keyCode = ke.getCode().toString();
			if((input.contains("SPACE") || keyCode.equals("SPACE")) && !(gameStatus.equals(GameStatus.END_OF_GAME) || gameStatus.equals(GameStatus.AFTER_WIN))) {
				if(ship.firePermission()) {
					if(!(laserTempList.isEmpty()) && ship.getLaserList().size() != laserTempList.size()) {
						ship.addLaser(laserTempList);
					}
				}
			}
			if(keyCode.equals("R") && !(gameStatus.equals(GameStatus.END_OF_GAME) || gameStatus.equals(GameStatus.AFTER_WIN))) {
				ship.reloading(laserTempList);
			}
			if((keyCode.equals("M") || keyCode.equals("ESCAPE")) && gameStatus.equals(GameStatus.START)) {
				gameAnimation.stop();
				gameCollision.stopCollision();
				if(bossStage.getBooleanValue()) {
					bossPlayer.pause();
				} else {
					gamePlayer.pause();
				}
				gameStage.setScene(menuScene);
				gameMenu.start();
				menuPlayer.play();
				gameStatus = GameStatus.PAUSE;
			}
			if(!input.contains(keyCode)) {
				input.add(keyCode);
			}
			});
		
		gameScene.setOnKeyReleased((ke) -> {
				String keyCode = ke.getCode().toString();
				input.remove(keyCode);
			});
		// console view
		Rectangle console = new Rectangle(-25, HEIGHT - 80, WIDTH + 50, 50);
		TextField consoleField = new TextField();
		consoleField.setPromptText("Console activated:");
		consoleField.setPrefSize(WIDTH - 15, 40);
		consoleField.setMaxSize(WIDTH, 40);
		consoleField.setId("console");
		consoleField.setLayoutY(HEIGHT - 80);
		// console event handler
		consoleField.setOnAction((ae) ->{
			consoleCommand = consoleField.getText();
			switch(consoleCommand) {
			case "myparentsarethebest":
				System.out.println("COMMAND ACCEPTED");
				ship.setHealth(899999999);
				consoleCommand = "";
				consoleField.clear();
				menu.getChildren().removeAll(console, consoleField);
				break;
			case "myfriendsarethebest":
				System.out.println("COMMAND ACCEPTED");
				ship.setHealth(899999999);
				consoleCommand = "";
				consoleField.clear();
				menu.getChildren().removeAll(console, consoleField);
				break;
			default:
				System.out.println("INVALID COMMAND");
				consoleField.clear();
				consoleField.setPromptText("Invalid command");
			}
		});
		
		menuScene.setOnKeyPressed((ke) -> {
			String keyCode = ke.getCode().toString();
			if(!(menu.getChildren().contains(console)) && (keyCode.equals("M") || keyCode.equals("ESCAPE")) && gameStatus.equals(GameStatus.PAUSE)) {
				gameMenu.stop();
				menuPlayer.stop();
				gameStage.setScene(gameScene);	
				gameAnimation.start();
				gameCollision.startCollision();
				if(bossStage.getBooleanValue()) {
					bossPlayer.play();
				} else {
					gamePlayer.play();
				}
				menu.getChildren().clear();
				menu.getChildren().addAll(menuCanvas, menuButtons, menuText, menuView, versionView);
				gameStatus = GameStatus.START;
			}
			if(!(menu.getChildren().contains(console)) && keyCode.equals("TAB")) {
				console.setFill(Color.BLACK);
				console.setOpacity(0.5);
				menu.getChildren().addAll(console, consoleField);
				System.out.println("CONSOLE ACTIVATED");
			} else if(menu.getChildren().contains(console) && keyCode.equals("TAB")) {
				menu.getChildren().removeAll(console, consoleField);
				System.out.println("CONSOLE DISABLED");
			}
			if(!input.contains(keyCode)) {
				input.add(keyCode);
			}
			});
		
		menuScene.setOnKeyReleased((ke) -> {
			String keyCode = ke.getCode().toString();
			input.remove(keyCode);
			});
		
		// restart or start new game from main menu
		newGameButton.setOnMouseClicked((me) -> {
			if(gameStatus.equals(GameStatus.BEFORE_FIRST_START)) {
				gameMenu.stop();
				menuPlayer.stop();
				gameStage.setScene(gameScene);	
				gameAnimation.start();
				gamePlayer.play();
			} else {
				gameAnimation.stop();
				gameCollision.stopCollision();
				if(bossStage.getBooleanValue()) {
					bossPlayer.stop();
					gameView.setMediaPlayer(gamePlayer);
				} else {
					gamePlayer.stop();
				}
				levelCleanup();
				gameMenu.stop();
				menuPlayer.stop();
				gameStage.setScene(gameScene);
				gameAnimation.start();
				gameCollision.startCollision();
				gamePlayer.play();
			}
			gameStatus = GameStatus.START;
			});
		
		aboutButton.setOnMouseClicked((me) -> {
			menu.getChildren().remove(menuButtons);
			Text tip = new Text(210, 148, aboutButton.getText());
			tip.setId("gamemenu");
			tip.setWrappingWidth(WIDTH - 236);
			tip.setTextAlignment(TextAlignment.CENTER);
			Text about = new Text(60, 200, "\tHello everyone!\n\n\tMy name is Stanislav Shergin (aka Claner/Cizerion). Thanks for playing this game!"
					+ " This is my \"Java skill researching\" project named as \"SP/ACE\\TEROIDS\". Game was grown through "
					+ "my Java learning interests and Java FX framework. For this projects i took free visual and sound game assets from different "
					+ "authors on different sites. All authors with appropriate links were specified in CREDITS.TXT file in my GitHub repository https://github.com/Cizerion/Spaceteroids"
					+ " or into \".jar\" file. "
					+ "\"SP/ACE\\TEROIDS\" is not commercial project and actually it has only studying Java and portfolio creating aims. "
					+ "Thanks to all who noticed this project!\n\n"
					+ "\tWith best regards, Stanislav.\n\nSP/ACE\\TEROIDS " + gameVersion);
			about.setId("gametext");
			about.setWrappingWidth(WIDTH - 120);
			about.setTextAlignment(TextAlignment.JUSTIFY);
			menu.getChildren().addAll(backButton, tip, about);
			});
		// just an experiment with elements positioning. Better to use completed png picture with all elements instead.
		controlsButton.setOnMouseClicked((me) -> {
			menu.getChildren().remove(menuButtons);
			Text tip = new Text(210, 148, controlsButton.getText());
			tip.setId("gamemenu");
			tip.setWrappingWidth(WIDTH - 236);
			tip.setTextAlignment(TextAlignment.CENTER);
			Image[] btns = {new Image("images/buttons/a-min.png"),
					new Image("images/buttons/d-min.png"),
					new Image("images/buttons/esc-min.png"),
					new Image("images/buttons/keys-min.png"),
					new Image("images/buttons/m-min.png"),
					new Image("images/buttons/r-min.png"),
					new Image("images/buttons/s-min.png"),
					new Image("images/buttons/space-min.png"),
					new Image("images/buttons/w-min.png")};
			ImageView[] ivButtons = new ImageView[btns.length];
			for(int i = 0; i < btns.length; i++) {
				ivButtons[i] = new ImageView(btns[i]);
				menu.getChildren().add(ivButtons[i]);
			}
			Text or = new Text(180, 258, "or");
			or.setId("gamemenu");
			Text moves = new Text(340, 258, "- moves");
			moves.setId("gamemenu");
			ivButtons[8].setX(95);ivButtons[8].setY(205);
			ivButtons[0].setX(64);ivButtons[0].setY(233);
			ivButtons[6].setX(95);ivButtons[6].setY(233);
			ivButtons[1].setX(126);ivButtons[1].setY(233);
			ivButtons[3].setX(230);ivButtons[3].setY(205);
			Text or2 = new Text(180, 325, "or");
			or2.setId("gamemenu");
			Text callMenu = new Text(340, 325, "- main menu");
			callMenu.setId("gamemenu");
			ivButtons[4].setX(95);ivButtons[4].setY(300);
			ivButtons[2].setX(261);ivButtons[2].setY(300);
			Text reloading = new Text(340, 390, "- reloading");
			reloading.setId("gamemenu");
			ivButtons[5].setX(261);ivButtons[5].setY(366);
			Text firing = new Text(340, 455, "- firing");
			firing.setId("gamemenu");
			ivButtons[7].setX(138);ivButtons[7].setY(432);
			menu.getChildren().addAll(backButton, tip, or, moves, or2, callMenu, reloading, firing);
			});
		
		scoreboardButton.setOnMouseClicked((me) -> {
			menu.getChildren().remove(menuButtons);
			loadScoreBoard();
			Text tip = new Text(210, 148, scoreboardButton.getText());
			tip.setId("gamemenu");
			tip.setWrappingWidth(WIDTH - 236);
			tip.setTextAlignment(TextAlignment.CENTER);			
			
			String name = "";
			String score = "";
			
		    for(int i = 0; i < (sb.length - 1); i++) {
		    	name += String.valueOf(i+1) + " \t" + sb[i].getName() + "\n";
		    	score += " \t" + sb[i].getScore() + "\n";
		    }
			
			Text nameView = new Text(180, 200, name);
			nameView.setId("scoreboard");
			nameView.setWrappingWidth(300);
			nameView.setTextAlignment(TextAlignment.LEFT);
			
			Text scoreView = new Text(300, 200, score);
			scoreView.setId("scoreboard");
			scoreView.setWrappingWidth(300);
			scoreView.setTextAlignment(TextAlignment.LEFT);
			
			menu.getChildren().addAll(backButton, tip, nameView, scoreView);
			});
		// set of checkboxes for menu -> options
		CheckBox cb = new CheckBox("Show labels");
		cb.setSelected(false);
		cb.setLayoutX(60);
		cb.setLayoutY(215);

		FlowPane options = new FlowPane(Orientation.VERTICAL);
		options.setVgap(10);
		options.setLayoutX(80);
		options.setLayoutY(250);
		Label[] lbOptions = {lbActivityTime, lbElapsedTime, lbFps, lbEEntities, lbTests2};
		CheckBox[] cbOptions = {new CheckBox("\"Activity Time\""),
								new CheckBox("\"Elapsed Time\""), 
								new CheckBox("\"FPS\""), 
								new CheckBox("\"Enemy entities\""),
								new CheckBox("\"Test label #2\"")};
		
		for(int i = 0; i < cbOptions.length; i++) {
			SimpleTypeWrapper iter = new SimpleTypeWrapper(i);
			cbOptions[i].setSelected(false);
			cbOptions[i].setDisable(true);
			cbOptions[i].setOnAction((ae) -> {
				if(cbOptions[iter.getIntValue()].isSelected()) {
					labels.getChildren().add(lbOptions[iter.getIntValue()]);
				} else {
					labels.getChildren().remove(lbOptions[iter.getIntValue()]);
				}
			});
			options.getChildren().add(cbOptions[i]);
		}
		
		cb.setOnAction((ae) -> {
				if(cb.isSelected()) {
					game.getChildren().add(labels);
					for(CheckBox t: cbOptions) {
						t.setDisable(false);
					}
				}
				else {
					game.getChildren().remove(labels);
					for(CheckBox t: cbOptions) {
						t.setDisable(true);
					}
				}
			});
				
		optionsButton.setOnMouseClicked((me) -> {
			menu.getChildren().remove(menuButtons);
			Text tip = new Text(210, 148, optionsButton.getText());
			tip.setId("gamemenu");
			tip.setWrappingWidth(WIDTH - 236);
			tip.setTextAlignment(TextAlignment.CENTER);		
			menu.getChildren().addAll(backButton, tip, cb, options);
			});
		
		// return to menu screen
		backButton.setOnMouseClicked((me) -> {
			menu.getChildren().clear();
			menu.getChildren().addAll(menuCanvas, menuButtons, menuText, menuView, versionView);
			});
				
		exitButton.setOnAction(e -> {
			gameCollision.stopCollisionDetectionThread();
			Platform.exit();
	        System.exit(0);
		});
		
		gameStage.setOnCloseRequest(e -> {
			gameCollision.stopCollisionDetectionThread();
			Platform.exit();
	        System.exit(0);
		});
		
		gameStage.show();
	}
}
