package me.shemplo.game.mankals.engine;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import me.shemplo.game.mankals.core.MankalsMain;
import me.shemplo.game.mankals.engine.logger.Log;

public class MainMenuScene {
	
	private MankalsMain _main;
	private Scene _gameScene;
	
	private Button startClassicGame;
	
	public MainMenuScene (Scene scene, MankalsMain main) {
		this._gameScene = scene;
		this._main = main;
		
		_init ();
	}
	
	private void _init () {
		this.startClassicGame = (Button) _gameScene.lookup ("#start_classic_game_button");
		startClassicGame.setOnMouseClicked (me -> {
			Log.message ("click");
			_main.switchScenes (_main.getStage (), MankalsMain.GAME_FRAME_MARKUP_FILE);
			_main.getGameEngine ().startNewGame ();
		});
	}
	
}
