package me.shemplo.game.mankals.engine;

import java.awt.Desktop;
import java.net.URI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import me.shemplo.game.mankals.core.MankalsMain;
import me.shemplo.game.mankals.engine.logger.Log;

public class AboutScene {
	
	private MankalsMain _main;
	private Scene _gameScene;
	
	private ScrollPane aboutScrollPane;
	private BorderPane rootBorderPane;
	private HBox windowTopMenuBox;
	private Label aboutMainLabel,
					toShemploLink;
	
	private Button toMainMenuButton,
					exitGame;
	
	private double dragStartX  = Integer.MAX_VALUE,
					dragStartY = Integer.MAX_VALUE;
	
	public AboutScene (Scene scene, MankalsMain main) {
		this._gameScene = scene;
		this._main = main;
		
		_init ();
	}
	
	private void _init () {
		this.rootBorderPane = (BorderPane) _gameScene.getRoot ();
		rootBorderPane.setBackground (new Background (new BackgroundFill (Color.rgb (245, 228, 197),
																			null,
																			null)));
		
		this.windowTopMenuBox = (HBox) _gameScene.lookup ("#window_menu_box");
		windowTopMenuBox.setBackground (new Background (new BackgroundFill (Color.rgb (230,210,174),
																				null,
																				null)));
		windowTopMenuBox.setOnMousePressed (me -> {
			dragStartX = me.getScreenX () - _main.getStage ().getX ();
			dragStartY = me.getScreenY () - _main.getStage ().getY ();
			Log.message ("Pointer on " + dragStartX + " / " + dragStartY);
		});
		
		windowTopMenuBox.setOnMouseDragged (me -> {
			_main.getStage ().setX (me.getScreenX () - dragStartX);
			_main.getStage ().setY (me.getScreenY () - dragStartY);
		});
		
		this.exitGame = (Button) _gameScene.lookup ("#exit_button");
		exitGame.setOnMouseClicked (me -> _main.exit ());
		
		this.aboutScrollPane = (ScrollPane) _gameScene.lookup ("#about_scroll_pane");
		aboutScrollPane.setHbarPolicy (ScrollBarPolicy.NEVER);
		
		this.aboutMainLabel = (Label) _gameScene.lookup ("#about_main_part");
		aboutMainLabel.setText ("Mancala, is a generic name for a family of 2-player "
									+ "turn-based strategy board games played with small stones, "
									+ "beans, or seeds and rows of holes or pits in the earth, "
									+ "a board or other playing surface. The objective is usually "
									+ "to capture all or some set of the opponent's stones, beans, etc. "
									+ "Versions of the game have been played for "
									+ "at least hundreds of years around the world."
									+ " (description is taken from wiki.com)");
		aboutMainLabel.setMaxWidth (aboutScrollPane.getWidth () - 30);
		aboutMainLabel.setWrapText (true);
		
		this.toShemploLink = (Label) _gameScene.lookup ("#to_shemplo_link");
		toShemploLink.setOnMouseClicked (me -> {
			Desktop desktop = Desktop.isDesktopSupported() 
								? Desktop.getDesktop() 
								: null;
			if (desktop != null 
					&& desktop.isSupported (Desktop.Action.BROWSE)) {
				try {
					desktop.browse (new URI ("https://github.com/Shemplo"));
				} catch (Exception e) {}
			}
		});
		
		this.toMainMenuButton = (Button) _gameScene.lookup ("#to_main_menu_button");
		toMainMenuButton.setOnMouseClicked (me -> {
			_main.switchScenes (_main.getStage (), MankalsMain.MAIN_MENU_MARKUP_FILE);
		});
	}
	
}
