package me.shemplo.game.mankals.engine;

import java.util.Arrays;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import javafx.scene.Cursor;
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
import javafx.scene.text.Font;
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
		
		String message = "> Game started";
		Log.message (message);
		gameLogList.getItems ().add (message);
		
		message = "> Turn of player: " + (currentPlayer + 1);
		Log.message (message);
		gameLogList.getItems ().add (message);
	}
	
	private final double horzOffset = 50,
							vertOffset = 75;
	private double cellWidth,
					cellHeight;
	private int deskLength,
				deskAmount,
				hoveredCell     = -1,
				prevHoveredCell = -2;
	private final Font CELLS_FONT = new Font (36);
	private final FontMetrics FONT_METRICS = Toolkit.getToolkit ()
													.getFontLoader ()
													.getFontMetrics (CELLS_FONT);
	
	private void _calculateBoundsForGame (int length, int mankals) {
		cellWidth = (canvasWidth - 2 * horzOffset) / (length + 2);
		cellHeight = (canvasHeight - 2 * vertOffset) / 2;
		deskAmount = mankals;
		deskLength = length;
		
		if (cellWidth <= CELLS_FONT.getSize ()) {
			Log.error ("Too big length of desk - game can't be started");
			gameLogList.getItems ().add ("Game can't be started: too big length of desk");
			isGameFinished = true;
		} else if (cellHeight <= CELLS_FONT.getSize ()) {
			Log.error ("Too big height of desk - game can't be started");
			gameLogList.getItems ().add ("Game can't be started: too big height of desk");
			isGameFinished = true;
		}
	}
	
	private void _drawScreen () {
		if (isGameFinished) { return; }
		
		_drawEmptyGrid ();
		
		if (hoveredCell != -1) {
			if (currentPlayer == 0) {
				context.setFill (Color.SALMON);
			} else {
				context.setFill (Color.WHITE);
			}
			
			context.fillRect (horzOffset + (hoveredCell + 1) * cellWidth + 1, 
								vertOffset + cellHeight + 1, 
								cellWidth - 2, 
								cellHeight - 2);
			_gameScene.setCursor (Cursor.HAND);
		} else {
			_gameScene.setCursor (Cursor.DEFAULT);
		}
		
		context.setStroke (Color.BLACK);
		context.setFill (Color.BLACK);
		context.setFont (CELLS_FONT);
		
		String tmp; double width;
		for (int i = 0; i < deskLength; i ++) {
			tmp = storage [currentPlayer][i] + "";
			width = FONT_METRICS.computeStringWidth (tmp);
			context.fillText (tmp, 
								horzOffset + (i + 1) * cellWidth + (cellWidth - width) / 2, 
								vertOffset + cellHeight * 5 / 4 + FONT_METRICS.getLineHeight ());
			
			tmp = storage [(currentPlayer + 1) % 2][deskLength - i - 1] + "";
			width = FONT_METRICS.computeStringWidth (tmp);
			context.fillText (tmp, 
								horzOffset + (i + 1) * cellWidth + (cellWidth - width) / 2, 
								vertOffset + cellHeight / 4 + FONT_METRICS.getLineHeight ());
		}
		
		tmp = base [(currentPlayer + 1) % 2] + "";
		width = FONT_METRICS.computeStringWidth (tmp);
		context.fillText (tmp, 
							horzOffset + (cellWidth - width) / 2, 
							vertOffset + cellHeight / 4 + FONT_METRICS.getLineHeight ());
		
		tmp = base [currentPlayer] + "";
		width = FONT_METRICS.computeStringWidth (tmp);
		context.fillText (tmp, 
							horzOffset + (deskLength + 1) * cellWidth + (cellWidth - width) / 2, 
							vertOffset + cellHeight * 5 / 4 + FONT_METRICS.getLineHeight ());
	}
	
	private void _drawEmptyGrid () {
		_clearScreen ();
		
		context.setStroke (Color.BLACK);
		
		double vs = vertOffset, 
				ve = vertOffset + 2 * cellHeight;
		for (int i = 0; i < deskLength + 3; i ++) {
			context.strokeLine (horzOffset + i * cellWidth, vs, 
									horzOffset + i * cellWidth, ve);
		}
		
		double hs = horzOffset,
				he = horzOffset + (deskLength + 2) * cellWidth;
		for (int i = 0; i < 4; i ++) {
			context.strokeLine (hs, vertOffset + i * cellHeight, 
									he, vertOffset + i * cellHeight);
		}
		
		context.strokeLine (horzOffset, 
								vertOffset + cellHeight * 3 / 2, 
								horzOffset + cellWidth,
								vertOffset + cellHeight * 3 / 2);
		context.strokeLine (horzOffset + cellWidth * (deskLength + 1), 
								vertOffset + cellHeight / 2, 
								horzOffset + cellWidth * (deskLength + 2), 
								vertOffset + cellHeight / 2);
		
		// Fields of 2 player
		if (currentPlayer == 1) {
			context.setFill (Color.CORNSILK);
		} else {
			context.setFill (Color.DARKSALMON);
		}
		for (int i = 0; i < deskLength; i ++) {
			context.fillRect (horzOffset + (i + 1) * cellWidth + 1, 
								vertOffset + 1, 
								cellWidth - 2, 
								cellHeight - 2);
		}
		context.fillRect (horzOffset + 1, 
							vertOffset + 1, 
							cellWidth - 2, 
							cellHeight * 3 / 2 - 2);
		
		// Fields of 1 player
		if (currentPlayer == 0) {
			context.setFill (Color.CORNSILK);
		} else {
			context.setFill (Color.DARKSALMON);
		}
		for (int i = 0; i < deskLength; i ++) {
			context.fillRect (horzOffset + (i + 1) * cellWidth + 1, 
								vertOffset + cellHeight + 1, 
								cellWidth - 2, 
								cellHeight - 2);
		}
		context.fillRect (horzOffset + cellWidth * (deskLength + 1) + 1, 
							vertOffset + cellHeight / 2 + 1, 
							cellWidth - 2, 
							cellHeight * 3 / 2 - 2);
		
		// Neutral cells
		context.setFill (Color.DARKGRAY);
		context.fillRect (horzOffset + cellWidth * (deskLength + 1) + 1, 
							vertOffset + 1, 
							cellWidth - 2, 
							cellHeight / 2 - 2);
		context.fillRect (horzOffset + 1, 
							vertOffset + cellHeight * 3 / 2 + 1, 
							cellWidth - 2, 
							cellHeight / 2 - 2);
	}
	
	private void _clearScreen () {
		if (context == null) { return; }
		
		context.clearRect (0, 0, canvasWidth, canvasHeight);
	}
	
	public void updateMouse (MouseEvent me) {
		int cell = _fetchCell (me.getSceneX (), me.getSceneY ());
		
		if (me.getClickCount () > 0) {
			if (cell != -1) { _cellSelected (cell); }
			_drawScreen ();
		} else {
			hoveredCell = cell;
			
			if (cell != -1) {
				if (hoveredCell != prevHoveredCell) {
					prevHoveredCell = hoveredCell;
				}
			} else {
				hoveredCell = -1;
				prevHoveredCell = -2;
			}
			
			_drawScreen ();
		}
	}
	
	private int _fetchCell (double x, double y) {
		if (x >= horzOffset + cellWidth && x < horzOffset + cellWidth * (deskLength + 1)
				&& y >= vertOffset + cellHeight && y < vertOffset + cellHeight * 2) {
			return (int) ((x - horzOffset - cellWidth) / cellWidth);
		}
		
		return -1;
	}
	
	private void _cellSelected (int cell) {
		String message = "* Player " + (currentPlayer + 1) + " selected cell #" + cell;
		Log.message (message);
		gameLogList.getItems ().add (message);
		
		if (storage [currentPlayer][cell] != 0) {
			int amount = storage [currentPlayer][cell];
			storage [currentPlayer][cell] = 0;
			
			int offset = cell + 1;
			int player = currentPlayer;
			
			while (amount != 0) {
				if (offset < deskLength) {
					storage [player][offset ++] ++;
				} else {
					if (player == currentPlayer) {
						base [player] ++;
						amount --;
					}
					
					amount ++;
					player = (player + 1) % 2;
					offset = 0;
				}
				
				amount --;
				_drawScreen ();
			}
			
			if (offset != 0 
					&& player == currentPlayer
					&& storage [currentPlayer][offset - 1] == 1
					&& storage [(currentPlayer + 1) % 2][deskLength - offset] != 0) {
				int got = 1 + storage [(currentPlayer + 1) % 2][deskLength - offset];
				storage [(currentPlayer + 1) % 2][deskLength - offset] = 0;
				storage [currentPlayer][offset - 1] = 0;
				base [currentPlayer] += got;
				
				message = "! Player " + (currentPlayer + 1) 
							+ " captured cell for "
							+ got + " points";
				Log.message (message);
				gameLogList.getItems ().add (message);
			}
			
			int winner = _fetchWinner ();
			if (_isEmptyStorage (0) 
					|| _isEmptyStorage (1)
					|| winner != -1) {
				message = "< Game finished";
				Log.message (message);
				gameLogList.getItems ().add (message);
				
				if (winner != -1) {
					message = "! Winner: player " + (winner + 1);
					Log.message (message);
					gameLogList.getItems ().add (message);
					
					message = "Due to: enough mankals in base";
					Log.message (message);
					gameLogList.getItems ().add (message);
				} else if (_isEmptyStorage (0)) {
					message = "! Winner: player " + (2);
					Log.message (message);
					gameLogList.getItems ().add (message);
					
					message = "Due to: opponent has drought";
					Log.message (message);
					gameLogList.getItems ().add (message);
				} else {
					message = "! Winner: player " + (1);
					Log.message (message);
					gameLogList.getItems ().add (message);
					
					message = "Due to: opponent has drought";
					Log.message (message);
					gameLogList.getItems ().add (message);
				}
				
				_drawScreen ();
				isGameFinished = true;
			} else if (!(offset == 0 && player != currentPlayer)) {
				currentPlayer = (currentPlayer + 1) % 2;
				currentTurn ++;
				
				message = "> Turn of player: " + (currentPlayer + 1);
				Log.message (message);
				gameLogList.getItems ().add (message);
			}
			
		} else {
			message = "This cell is empty";
			Log.message (message);
			gameLogList.getItems ().add (message);
		}
		
		_drawScreen ();
	}
	
	private boolean _isEmptyStorage (int player) {
		for (int i = 0; i < deskLength; i ++) {
			if (storage [player][i] != 0) {
				return false;
			}
		}
		
		return true;
	}
	
	private int _fetchWinner () {
		if (base [0] >= (deskLength * deskAmount * 2) / 2 + 1) {
			return 0;
		} else if (base [1] >= (deskLength * deskAmount * 2) / 2 + 1) {
			return 1;
		}
		
		return -1;
	}
	
}
