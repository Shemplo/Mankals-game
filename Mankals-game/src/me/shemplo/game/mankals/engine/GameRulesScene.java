package me.shemplo.game.mankals.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import me.shemplo.game.mankals.core.MankalsMain;
import me.shemplo.game.mankals.engine.logger.Log;

public class GameRulesScene {

	private MankalsMain _main;
	private Scene _gameScene;
	
	private ScrollPane rulesScrollPane;
	private BorderPane rootBorderPane;
	private HBox windowTopMenuBox;
	@SuppressWarnings ("unused")
	private VBox rulesListBox;
	
	private Button toMainMenuButton,
					exitGame;
	
	private double dragStartX  = Integer.MAX_VALUE,
					dragStartY = Integer.MAX_VALUE;
	
	public GameRulesScene (Scene scene, MankalsMain main) {
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
		
		this.rulesScrollPane = (ScrollPane) _gameScene.lookup ("#rules_scroll_pane");
		rulesScrollPane.setHbarPolicy (ScrollBarPolicy.NEVER);
		
		this.rulesListBox = (VBox) _gameScene.lookup ("#rules_list_v_box");
		
		this.toMainMenuButton = (Button) _gameScene.lookup ("#to_main_menu_button");
		toMainMenuButton.setOnMouseClicked (me -> {
			_main.switchScenes (_main.getStage (), MankalsMain.MAIN_MENU_MARKUP_FILE);
		});
		
		try (
			InputStream is = ClassLoader.getSystemResourceAsStream (MankalsMain.GAME_RULES_TEXT_FILE);
			InputStreamReader isr = new InputStreamReader (is, "UTF-8");
			BufferedReader br = new BufferedReader (isr);
		) {
			for (int i = 0; i < 14; i ++) {
				Label label = (Label) _gameScene.lookup ("#rules_label_" + i);
				label.setMaxWidth (rulesScrollPane.getWidth () - 30);
				label.setWrapText (true);
				
				label.setText (br.readLine ());
			}
		} catch (IOException e) {
			// Oooo-ps
		}
		
		
	}
	
}
