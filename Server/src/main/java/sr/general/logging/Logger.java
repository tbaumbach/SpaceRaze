/*
 * Created on 2004-nov-08 
 */
package sr.general.logging;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.logging.Level;

import sr.server.properties.PropertiesHandler;
import sr.world.Galaxy;

/**
 * This class handles all logging
 * Currently logs to System.out
 * 
 * Usage examples:
 * -severe: errors
 * -warning: lesser errors/exceptions
 * -info: general info messages
 * -config: startup/configuration messages
 * -fine: method heads, methods called in GalaxyUpdater.performUpdate(), not inside loops
 * -finer: method heads called inside loops, detail messages
 * -finest: in large loops, details in complex algorithms etc
 */
public class Logger {
	private static Level logLevel;
	private static boolean doOutput = true;
	private static String basePath; // inlagd för att fixa skum bugg, borde egentligen inte behövas...
	private static Galaxy galaxy;

	public static void setLogLevel(String newLevelString){
		System.out.println("Logger.setLogLevel(" + newLevelString + ")");
		logLevel = parseLevelString(newLevelString);
	}	
	
	private static Level parseLevelString(String levelString){
		return Level.parse(levelString.toUpperCase());
	}
	
	public static void setGalaxy(Galaxy aGalaxy){
		galaxy = aGalaxy;
	}
	
	public static void setDoOutput(boolean newDoOutput){
		if (!newDoOutput){
			fine("LoggingHandler.doOutput set to false");
		}
		doOutput = newDoOutput;
		if (newDoOutput){
			fine("LoggingHandler.doOutput set to true");
		}
	}
	
	public static void off(String message){
		log(message,Level.OFF);
	}

	public static void severe(String message){
		log(message,Level.SEVERE);
	}

	public static void warning(String message){
		log(message,Level.WARNING);
	}

	public static void info(String message){
		log(message,Level.INFO);
	}

	public static void config(String message){
		log(message,Level.CONFIG);
	}

	public static void fine(String message){
		log(message,Level.FINE);
	}

	public static void finer(String message){
		log(message,Level.FINER);
	}

	public static void finest(String message){
		log(message,Level.FINEST);
	}

	public static void all(String message){
		log(message,Level.ALL);
	}

	private static void log(String message, Level level){
		if (doOutput){
			// set the log level
			if (logLevel == null){
				String logLevelStr = "fine"; // default log level om ingen propertiesfil hittas
				try{
					logLevelStr = PropertiesHandler.getProperty("loglevel");
				}catch(MissingResourceException mre){
					System.out.println("Setting log level to default: " + logLevelStr);
				}
				setLogLevel(logLevelStr);
			}
			Thread curThread = Thread.currentThread();
			StackTraceElement[] traces = curThread.getStackTrace();
			// find first row in stacktrace that doesn't belong in the logger
			int stackSize = traces.length;
			int counter = 2; // skip the first two traces since they must be the Thread.getStackTrace and one log call
			StackTraceElement foundStackTrace = null;
			while ((foundStackTrace == null) & (counter < stackSize)){
				StackTraceElement element = traces[counter];
//				System.out.println("stacktrace: " + element.getClass().getCanonicalName());
				String className = element.getClassName().substring(element.getClassName().lastIndexOf(".")+1); 
				if (!className.equals("Logger")){
					foundStackTrace = element;
//					System.out.println("stacktrace found: " + element.toString());
				}else{
					counter++;
				}
			}
			String callingClass = foundStackTrace.getClassName().substring(foundStackTrace.getClassName().lastIndexOf(".")+1); 
			String callingMethod = foundStackTrace.getMethodName();
			String line = String.valueOf(foundStackTrace.getLineNumber());
			Date aDate = new Date();
			SimpleDateFormat aSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
			String time = aSimpleDateFormat.format(aDate);
			// creates log messages like this:
			//	[WARNING] 22:21:29 (BlackMarketPopupPanel:setSumfieldFocus:124)       Här är meddelandet
			String logStr = "[" + padWithBlanks(level.toString(),7) + "] " + time + " (" + callingClass + ".java:" + callingMethod + ":" + line + ") ";
//			logStr = padWithBlanks(logStr,70) + message;
			logStr += message;
			// maybe add to galaxy log
			// maybe log to out
			if (level.intValue() >= logLevel.intValue()){
				System.out.println(logStr);
				if (galaxy != null){ 
					// if game exist to log to, always add to last log
					galaxy.addToLastLog(logStr + "\n");
				}
			}
		}
	}
	
	private static String padWithBlanks(String original, int length){
		String retVal = null;
		if (original.length() > length){
			retVal = original;
		}else{
			retVal = original + padWithBlanks2(original, length);
		}
		return retVal;
	}
	
	private static String padWithBlanks2(String original, int length){
		String blanks = "";
		if(original != null){
			for (int i = 0; (i < length - original.length()); i++){
				blanks += " ";
			}
		}
		return blanks;
	}

	public static String getLogFilesString(){
		List<String> allLogs = getAllLogFiles();
		String logsStr = "";
		if (allLogs.size() == 0){
			logsStr = "No log files found...";
		}else{
			for (String fileName : allLogs){
				logsStr = logsStr + "<a href=\"view_log.jsp?logname=" + fileName + "\">" + fileName + "</a><br>";
			}
		}
		return logsStr;
	}
	
	/**
	 * Returns a list of all log file names in the tomcat logs folder
	 * @return
	 */
	private static List<String> getAllLogFiles(){
		if (basePath == null){
			basePath = PropertiesHandler.getProperty("basepath");
		}
		Logger.finer("basePath: " + basePath);
		String completePath = basePath + "..\\..\\logs\\";
		Logger.finest("LoggingHandler.getAllLogFiles: folderPath=" + completePath);
		List<String> allLogFileNames = new LinkedList<String>();
		File propFolder = new File(completePath);
		if (propFolder.exists()){
			File[] propFiles = propFolder.listFiles(); 
			for (int i = 0; i < propFiles.length; i++) {
				File file = propFiles[i];
				if (!file.isDirectory()){
					String mapName = file.getName();
					allLogFileNames.add(mapName);
				}
			}
		}

		return allLogFileNames;
	}
	
}
