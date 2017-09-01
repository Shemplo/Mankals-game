package me.shemplo.game.mankals.core;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.shemplo.game.mankals.engine.MankalsEngine;
import me.shemplo.game.mankals.engine.logger.Log;

public class MankalsMain extends Application {
	
	/* ===| INIT AREA |=== */
	
	public static final String GAME_FRAME_TITLE = "Mankals game";
	public static final String GAME_FRAME_MARKUP_FILE = "me/shemplo/game/mankals/engine/schemas/game-frame.fxml";
	
	public static final double GAME_FRAME_WIDTH   = 900,
								GAME_FRAME_HEIGHT = 400;
	
	public static void main (String... args) {
		Log.message ("Launching main frame");
		launch (args); // Launching main frame
	}
	
	public static void fatalStop (int code) {
		System.err.println ("Some falal error");
		Log.close (); // Flush to files
		System.exit (1);
	}
	
	/* ===| APPLICATION AREA |=== */
	
	private Parent parent;
	private Scene scene;
	
	public void start (Stage stage) {
		try {
			URL resource = ClassLoader.getSystemResource (GAME_FRAME_MARKUP_FILE);
			parent = FXMLLoader.load (resource);
		} catch (IOException | NullPointerException ioe) {
			Log.error ("Markup file `" 
						+ GAME_FRAME_MARKUP_FILE 
						+ "` not found");
			fatalStop (1);
		}
		
		scene = new Scene (parent, GAME_FRAME_WIDTH, GAME_FRAME_HEIGHT);
		MankalsEngine engine = new MankalsEngine (scene, this);
		Log.message ("Game frame scene inited");
		stage.setScene (scene);
		
		// WARNING: tmp code
		engine.startNewGame ();
		scene.setOnMouseMoved (me -> {
			engine.updateMouse (me);
		});
		scene.setOnMouseClicked (me -> {
			engine.updateMouse (me);
		});
		
		stage.show ();
		stage.setResizable (false);
		stage.setTitle (GAME_FRAME_TITLE);
		stage.setOnCloseRequest (cre -> {
			Log.close ();
		});
		Log.message ("Main frame launched - OK");
	}
	
}
