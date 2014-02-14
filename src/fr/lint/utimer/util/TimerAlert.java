package fr.lint.utimer.util;

/**
 * Implement this class to handle the Timer stop event.
 * @author sdarocha
 *
 */
public interface TimerAlert {
	public void onTimerStop();
	public void onTimerStart();
	public void onTimerPause();
	public void onTimerForcedStop();

}
