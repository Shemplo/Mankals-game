package me.shemplo.game.mankals.engine;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import me.shemplo.game.mankals.core.MankalsMain;
import me.shemplo.game.mankals.engine.logger.Log;

public class MainMenuScene {
	
	private MankalsMain _main;
	private Scene _gameScene;
	
	private BorderPane rootBorderPane;
	
	@SuppressWarnings ("unused")
	private Button startClassicGame,
					startCustomGame,
					startGameWithAI,
					seeRules,
					seeAbout,
					exitGame;
	
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
		
		this.startClassicGame = (Button) _gameScene.lookup ("#start_classic_game_button");
		startClassicGame.setOnMouseClicked (me -> {
			Log.message ("click");
			_main.switchScenes (_main.getStage (), MankalsMain.GAME_FRAME_MARKUP_FILE);
			_main.getGameEngine ().startNewGame ();
		});
		
		this.startCustomGame = (Button) _gameScene.lookup ("#start_custom_game_button");
		
		this.exitGame = (Button) _gameScene.lookup ("#exit_button");
		exitGame.setOnMouseClicked (me -> _main.exit ());
	}
	
}
