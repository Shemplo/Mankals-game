package me.shemplo.game.mankals.engine;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import me.shemplo.game.mankals.core.MankalsMain;
import me.shemplo.game.mankals.engine.logger.Log;

public class CustomGameMenuScene {

	private MankalsMain _main;
	private Scene _gameScene;
	
	private BorderPane rootBorderPane;
	private HBox windowTopMenuBox;
	
	private Button exitGame,
					toMainMenuButton,
					startGameButton;
	private Slider cellsNumberSlider,
					cellsSizeSlider;
	
	private double dragStartX  = Integer.MAX_VALUE,
					dragStartY = Integer.MAX_VALUE;
	
	public CustomGameMenuScene (Scene scene, MankalsMain main) {
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
		
		this.cellsNumberSlider = (Slider) _gameScene.lookup ("#cells_number_slider");
		
		this.cellsSizeSlider = (Slider) _gameScene.lookup ("#cells_size_slider");
		
		this.toMainMenuButton = (Button) _gameScene.lookup ("#to_main_menu_button");
		toMainMenuButton.setOnMouseClicked (me -> {
			_main.switchScenes (_main.getStage (), MankalsMain.MAIN_MENU_MARKUP_FILE);
		});
		
		this.startGameButton = (Button) _gameScene.lookup ("#start_game_button");
		startGameButton.setOnMouseClicked (me -> {
			int cells  = (int) Math.round (cellsNumberSlider.getValue ());
			int amount = (int) Math.round (cellsSizeSlider.getValue ());
			Log.message ("New game: " + cells + " cells, " + amount + " mankals");
			
			_main.switchScenes (MankalsMain.GAME_FRAME_MARKUP_FILE);
			_main.getGameEngine ().startNewGame (cells, amount);
		});
	}
	
}
