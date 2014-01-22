package com.sdarocha.sdtimer.util;

/**
 * Implement this class to get notified of the time ellapse every second.
 * @author sdarocha
 *
 */
public abstract class Monitor {
	/**
	 * This method will be called avery second by {@link Timer}
	 * @param initialTime initial count down time
	 * @param currentTime time to run until the end of the timer
	 */
	public abstract void Refresh( long initialTime, long currentTime);
}
