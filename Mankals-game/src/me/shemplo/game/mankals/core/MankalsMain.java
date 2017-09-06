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
import javafx.stage.StageStyle;
import me.shemplo.game.mankals.engine.CustomGameMenuScene;
import me.shemplo.game.mankals.engine.GameRulesScene;
import me.shemplo.game.mankals.engine.MainMenuScene;
import me.shemplo.game.mankals.engine.MankalsEngine;
import me.shemplo.game.mankals.engine.logger.Log;

public class MankalsMain extends Application {
	
	/* ===| INIT AREA |=== */
	
	public static final String GAME_FRAME_TITLE = "Mankals game";
	
	public static final String CORE_PATH = "me/shemplo/game/mankals";
	public static final String SCHEMAS_PATH = CORE_PATH + "/engine/schemas";
	public static final String STYLES_PATH = CORE_PATH + "/engine/styles";
	
	public static final String CUSTOM_GAME_MENU_MARKUP_FILE = SCHEMAS_PATH + "/custom-game-menu-frame.fxml";
	public static final String GAME_RULES_MARKUP_FILE = SCHEMAS_PATH + "/game-rules-frame.fxml";
	public static final String MAIN_MENU_MARKUP_FILE  = SCHEMAS_PATH + "/main-menu-frame.fxml";
	public static final String GAME_FRAME_MARKUP_FILE = SCHEMAS_PATH + "/game-frame.fxml";
	
	public static final String GAME_RULES_TEXT_FILE = SCHEMAS_PATH + "/rules.txt";
	
	public static final String BORDERS_STYLES_FILE = STYLES_PATH + "/borders.css";
	public static final String BUTTONS_STYLES_FILE = STYLES_PATH + "/buttons.css";
	public static final String FONTS_STYLES_FILE = STYLES_PATH + "/fonts.css";
	
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
	private String cssBorders,
					cssButtons,
					cssFonts;
	
	public void start (Stage stage) {
		this.stage = stage;
		this.cssBorders = ClassLoader.getSystemResource (BORDERS_STYLES_FILE).toExternalForm ();
		this.cssButtons = ClassLoader.getSystemResource (BUTTONS_STYLES_FILE).toExternalForm ();
		this.cssFonts   = ClassLoader.getSystemResource (FONTS_STYLES_FILE).toExternalForm ();
		
		switchScenes (stage, MAIN_MENU_MARKUP_FILE);
		Log.message ("Main frame launched - OK");
		
		stage.setOnCloseRequest (cre -> Log.close ());
		stage.initStyle (StageStyle.UNDECORATED);
		stage.setTitle (GAME_FRAME_TITLE);
		stage.setResizable (false);
		stage.show ();
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
	private MankalsEngine gameEngine;
	
	public void switchScenes (String markupFile) {
		switchScenes (getStage (), markupFile);
	}
	
	public void switchScenes (Stage stage, String markupFile) {
		if (!loadedScenes.containsKey (markupFile)
				|| loadedScenes.get (markupFile) != null) {
			Scene scene = makeScene (markupFile);
			loadedScenes.put (markupFile, scene);
		}
		
		Scene scene = loadedScenes.get (markupFile);
		
		if (scene != null) {
			scene.getStylesheets ().add (cssBorders);
			scene.getStylesheets ().add (cssButtons);
			scene.getStylesheets ().add (cssFonts);
			stage.setScene (scene);
		
			switch (markupFile) {
				case MAIN_MENU_MARKUP_FILE:
					new MainMenuScene (scene, this);
					break;
					
				case GAME_FRAME_MARKUP_FILE:
					gameEngine = new MankalsEngine (scene, this);
					scene.setOnMouseMoved   (me -> gameEngine.updateMouse (me));
					scene.setOnMouseClicked (me -> gameEngine.updateMouse (me));
					break;
					
				case CUSTOM_GAME_MENU_MARKUP_FILE:
					new CustomGameMenuScene (scene, this);
					break;
					
				case GAME_RULES_MARKUP_FILE:
					new GameRulesScene (scene, this);
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
			ioe.printStackTrace ();
			return null;
		}

		return new Scene (parent, GAME_FRAME_WIDTH, GAME_FRAME_HEIGHT);
	}
	
}
