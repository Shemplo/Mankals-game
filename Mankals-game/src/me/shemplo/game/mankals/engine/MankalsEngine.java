package me.shemplo.game.mankals.engine;

import java.util.Arrays;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import me.shemplo.game.mankals.core.MankalsMain;
import me.shemplo.game.mankals.engine.logger.Log;

public class MankalsEngine {
	
	private MankalsMain _main;
	private Scene _gameScene;
	
	private ListView <String> gameLogList;
	private GraphicsContext context;
	private BorderPane borderPane;
	private StackPane stackPane;
	private Canvas canvas;
	
	private double canvasWidth, canvasHeight;
	private int currentTurn, currentPlayer;
	private boolean isGameFinished = false;
	
	private int [][] storage;
	private int [] base;
	
	public MankalsEngine (Scene scene, MankalsMain main) {
		this._gameScene = scene;
		this._main = main;
		_init ();
	}
	
	@SuppressWarnings ("unchecked")
	private void _init () {		
		this.borderPane = (BorderPane) _gameScene.lookup ("#game_border_pane");
		this.stackPane = (StackPane) _gameScene.lookup ("#game_stack_pane");
		
		// It looks - OK
		borderPane.setBackground (new Background (new BackgroundFill (Color.WHEAT, null, null)));
		
		this.gameLogList = (ListView <String>) _gameScene.lookup ("#game_log_list");
		gameLogList.setFocusTraversable (false);
		
		this.canvas = (Canvas) _gameScene.lookup ("#game_canvas");
		canvas.setWidth (canvasWidth = (MankalsMain.GAME_FRAME_WIDTH * 3 / 4 - 10));
		canvas.setHeight (canvasHeight = MankalsMain.GAME_FRAME_HEIGHT);
		this.context = canvas.getGraphicsContext2D ();
		
		Log.message ("Canvas bounds: " + canvasWidth + " | " + canvasHeight);
	}
	
	public void startNewGame () {
		// This is default game parameters
		startNewGame (6, 4);
	}
	
	public void stopCurrentGame () {
		
	}
	
	public void startNewGame (int length, int mankals) {
		gameLogList.getItems ().clear ();
		_calculateBoundsForGame (length, mankals);
		
		storage = new int [2][length];
		for (int i = 0; i < storage.length; i ++) {
			Arrays.fill (storage [i], mankals);
		}
		
		base = new int [2];
		currentPlayer = 0;
		currentTurn = 0;
		
		_drawScreen ();
	}
	
	private final double horzOffset = 65,
							vertOffset = 75;
	private double fieldWidth,
					fieldHeight;
	private int deskLength;
	
	private void _calculateBoundsForGame (int length, int mankals) {
		fieldWidth = (canvasWidth - 2 * horzOffset) / (length + 2);
		fieldHeight = (canvasHeight - 2 * vertOffset) / 2;
		deskLength = length;
		
		if (fieldWidth <= 20) {
			Log.error ("Too big length of desk - game can't be started");
			gameLogList.getItems ().add ("Game can't be started: too big length of desk");
			isGameFinished = true;
		} else if (fieldHeight <= 20) {
			Log.error ("Too big height of desk - game can't be started");
			gameLogList.getItems ().add ("Game can't be started: too big height of desk");
			isGameFinished = true;
		}
	}
	
	private void _drawScreen () {
		_drawEmptyGrid ();
	}
	
	private void _drawEmptyGrid () {
		_clearScreen ();
		
		context.setStroke (Color.BLACK);
		
		double vs = vertOffset, 
				ve = vertOffset + 2 * fieldHeight;
		for (int i = 0; i < deskLength + 3; i ++) {
			context.strokeLine (horzOffset + i * fieldWidth, vs, 
									horzOffset + i * fieldWidth, ve);
		}
		
		double hs = horzOffset,
				he = horzOffset + 8 * fieldWidth;
		for (int i = 0; i < 4; i ++) {
			context.strokeLine (hs, vertOffset + i * fieldHeight, 
									he, vertOffset + i * fieldHeight);
		}
		
		context.strokeLine (horzOffset, 
								vertOffset + fieldHeight * 3 / 2, 
								horzOffset + fieldWidth,
								vertOffset + fieldHeight * 3 / 2);
		context.strokeLine (horzOffset + fieldWidth * 7, 
								vertOffset + fieldHeight / 2, 
								horzOffset + fieldWidth * 8, 
								vertOffset + fieldHeight / 2);
		
		// Fields of 2 player
		context.setFill (Color.CORNSILK);
		for (int i = 0; i < deskLength; i ++) {
			context.fillRect (horzOffset + (i + 1) * fieldWidth + 1, 
								vertOffset + 1, 
								fieldWidth - 2, 
								fieldHeight - 2);
		}
		context.fillRect (horzOffset + 1, 
							vertOffset + 1, 
							fieldWidth - 2, 
							fieldHeight * 3 / 2 - 2);
		
		// Fields of 1 player
		context.setFill (Color.SALMON);
		for (int i = 0; i < deskLength; i ++) {
			context.fillRect (horzOffset + (i + 1) * fieldWidth + 1, 
								vertOffset + fieldHeight + 1, 
								fieldWidth - 2, 
								fieldHeight - 2);
		}
		context.fillRect (horzOffset + fieldWidth * 7 + 1, 
							vertOffset + fieldHeight / 2 + 1, 
							fieldWidth - 2, 
							fieldHeight * 3 / 2 - 2);
		
		context.setFill (Color.DARKGRAY);
		context.fillRect (horzOffset + fieldWidth * 7 + 1, 
							vertOffset + 1, 
							fieldWidth - 2, 
							fieldHeight / 2 - 2);
		context.fillRect (horzOffset + 1, 
							vertOffset + fieldHeight * 3 / 2 + 1, 
							fieldWidth - 2, 
							fieldHeight / 2 - 2);
	}
	
	private void _clearScreen () {
		if (context == null) { return; }
		
		context.clearRect (0, 0, canvasWidth, canvasHeight);
	}
	
	public void updateMouse (MouseEvent me) {
		if (me.getClickCount () > 0) {
			_drawScreen ();
		}
	}
	
	private void _cellSelected (int cell) {
		
	}
	
}
