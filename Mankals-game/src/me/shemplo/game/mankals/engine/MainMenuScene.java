package me.shemplo.game.mankals.engine;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import me.shemplo.game.mankals.core.MankalsMain;
import me.shemplo.game.mankals.engine.logger.Log;

public class MainMenuScene {
	
	private MankalsMain _main;
	private Scene _gameScene;
	
	private BorderPane rootBorderPane;
	private HBox windowTopMenuBox;
	
	@SuppressWarnings ("unused")
	private Button startClassicGame,
					startCustomGame,
					startGameWithAI,
					seeRules,
					seeAbout,
					exitGame;
	
	private double dragStartX  = Integer.MAX_VALUE,
					dragStartY = Integer.MAX_VALUE;
	
	public MainMenuScene (Scene scene, MankalsMain main) {
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
		
		this.startClassicGame = (Button) _gameScene.lookup ("#start_classic_game_button");
		startClassicGame.setOnMouseClicked (me -> {
			_main.switchScenes (MankalsMain.GAME_FRAME_MARKUP_FILE);
			_main.getGameEngine ().startNewGame ();
		});
		
		this.startCustomGame = (Button) _gameScene.lookup ("#start_custom_game_button");
		startCustomGame.setOnMouseClicked (me -> {
			_main.switchScenes (MankalsMain.CUSTOM_GAME_MENU_MARKUP_FILE);
		});
		
		this.seeRules = (Button) _gameScene.lookup ("#see_rules_button");
		seeRules.setOnMouseClicked (me -> {
			_main.switchScenes (MankalsMain.GAME_RULES_MARKUP_FILE);
		});
		
		this.seeAbout = (Button) _gameScene.lookup ("#see_about_button");
		seeAbout.setOnMouseClicked (me -> {
			_main.switchScenes (MankalsMain.ABOUT_MARKUP_FILE);
		});
		
		this.exitGame = (Button) _gameScene.lookup ("#exit_button");
		exitGame.setOnMouseClicked (me -> _main.exit ());
	}
	
}
