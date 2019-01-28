package com.revature;

import org.apache.log4j.Logger;

/**
 * Log4j helps save logs of what is happening during the runtime of the code. This helps maintain
 * and debug the code.
 * @author Kyne Liu
 *
 */
public class LoggerLauncher {
	static Logger log = Logger.getRootLogger();
	
	public static void main(String[] args) {
		//log4j logging levels from lowest to highest. The higher level logs do not print the lower level
		//logs. This setting is set in the properties file for log4j
		
		//very fine grained details on the app state. Basically logging everything that is happening
		log.trace("This is the trace log");
		//general debugging output
		log.debug("This is the debug log");
		//information about app state changes
		log.info("This is the info log");
		//app is doing something it should not be doing like using legacy code but is still working
		log.warn("This is the warning log");
		//something went wrong but app is still running
		log.error("This is the error log");
		//entire app crashed
		log.fatal("This is the fatal log");
	}
}
