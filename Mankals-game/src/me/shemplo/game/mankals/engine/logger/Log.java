package me.shemplo.game.mankals.engine.logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.shemplo.game.mankals.core.MankalsMain;

public class Log {

	private static final File _messageFile = new File ("logs/messages.log"),
								_errorFile = new File ("logs/errors.log");
	private static PrintWriter msWriter = null, 
								erWriter = null;
	
	private static DateFormat logTime = new SimpleDateFormat ("HH:mm:ss dd.MM.yyyy");
	
	static {
		File folder = new File ("logs");
		if (!folder.exists ()) { folder.mkdir (); }
		
		try {
			if (!_messageFile.exists ()) {
				_messageFile.createNewFile ();
			}
			
			if (!_errorFile.exists ()) {
				_errorFile.createNewFile ();
			}
		} catch (IOException ioe) {
			System.err.println ("Failed to create log file: " + ioe.getMessage ());
			MankalsMain.fatalStop (1);
		}
		
		try {
			msWriter = new PrintWriter (_messageFile, "UTF-8");
		} catch (FileNotFoundException fnfe) {
			// It's impossible, but who knows
			MankalsMain.fatalStop (1);
		} catch (UnsupportedEncodingException uee) {
			// It's impossible, but who knows
			MankalsMain.fatalStop (1);
		}
		
		try {
			erWriter = new PrintWriter (_errorFile, "UTF-8");
		} catch (FileNotFoundException fnfe) {
			// It's impossible, but who knows
			MankalsMain.fatalStop (1);
		} catch (UnsupportedEncodingException uee) {
			// It's impossible, but who knows
			MankalsMain.fatalStop (1);
		}
	}
	
	public static void message (String content) {
		synchronized (msWriter) {
			msWriter.flush ();
			
			String message = logTime.format (new Date ()) + " " + content;
			msWriter.println (message);
			System.out.println (message);
		}
	}
	
	public static void error (String content) {
		
	}
	
	public static void error (Exception exception) {
		
	}
	
	public static void close () {
		if (msWriter != null) {
			msWriter.close ();
		}
		
		if (erWriter != null) {
			erWriter.close ();
		}
	}
	
}
