package me.shemplo.game.mankals.core;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.shemplo.game.mankals.engine.MainMenuScene;
import me.shemplo.game.mankals.engine.MankalsEngine;
import me.shemplo.game.mankals.engine.logger.Log;

public class MankalsMain extends Application {
	
	/* ===| INIT AREA |=== */
	
	public static final String GAME_FRAME_TITLE = "Mankals game";
	public static final String MAIN_MENU_MARKUP_FILE  = "me/shemplo/game/mankals/engine/schemas/main-menu-frame.fxml";
	public static final String GAME_FRAME_MARKUP_FILE = "me/shemplo/game/mankals/engine/schemas/game-frame.fxml";
	
	public static final String BUTTONS_STYLES_FILE = "me/shemplo/game/mankals/engine/styles/buttons.css";
	
	public static final String HAND_ICON_IMAGE_FILE = "me/shemplo/game/mankals/about/hold.png";
	
	public static final double GAME_FRAME_WIDTH   = 800,
								GAME_FRAME_HEIGHT = 400;
	
	public static void main (String... args) {
		Log.message ("Launching main frame");
		launch (args); // Launching main frame
	}
	
	public static void fatalStop (int code) {
		System.err.println ("Falal error");
		Log.close (); // Flush to files
		System.exit (1);
	}
	
	/* ===| APPLICATION AREA |=== */
	
	private Stage stage;
	private String css;
	
	public void start (Stage stage) {
		this.stage = stage;
		this.css = ClassLoader.getSystemResource (BUTTONS_STYLES_FILE).toExternalForm ();
		
		switchScenes (stage, MAIN_MENU_MARKUP_FILE);
		Log.message ("Main frame launched - OK");
		
		stage.show ();
		stage.setResizable (false);
		stage.setTitle (GAME_FRAME_TITLE);
		stage.setOnCloseRequest (cre -> Log.close ());
	}
	
	public void exit () {
		Log.close   ( );
		System.exit (0);
	}
	
	public Stage getStage () {
		return stage;
	}
	
	public MankalsEngine getGameEngine () {
		return gameEngine;
	}
	
	private Map <String, Scene> loadedScenes = new HashMap <> ();
	
	@SuppressWarnings ("unused")
	private MainMenuScene mainMenuScene;
	private MankalsEngine gameEngine;
	
	public void switchScenes (Stage stage, String markupFile) {
		if (!loadedScenes.containsKey (markupFile)
				|| loadedScenes.get (markupFile) != null) {
			Scene scene = makeScene (markupFile);
			loadedScenes.put (markupFile, scene);
		}
		
		Scene scene = loadedScenes.get (markupFile);
		scene.getStylesheets ().add (css);
		
		if (scene != null) {
			stage.setScene (scene); 
		
			switch (markupFile) {
				case MAIN_MENU_MARKUP_FILE:
					mainMenuScene = new MainMenuScene (scene, this);
					break;
					
				case GAME_FRAME_MARKUP_FILE:
					gameEngine = new MankalsEngine (scene, this);
					scene.setOnMouseMoved   (me -> gameEngine.updateMouse (me));
					scene.setOnMouseClicked (me -> gameEngine.updateMouse (me));
					break;
			}
		}
	}
	
	private Scene makeScene (String markupFile) {
		Parent parent;
		
		try {
			URL resource = ClassLoader.getSystemResource (markupFile);
			parent = FXMLLoader.load (resource);
		} catch (IOException | NullPointerException ioe) {
			Log.error ("Markup file `" 
						+ markupFile
						+ "` not found");
			return null;
		}

		return new Scene (parent, GAME_FRAME_WIDTH, GAME_FRAME_HEIGHT);
	}
	
}
