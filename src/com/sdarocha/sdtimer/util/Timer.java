package com.sdarocha.sdtimer.util;

import java.util.Iterator;
import java.util.LinkedList;

import android.os.CountDownTimer;

/**
 * Class used to count down seconds.
 * The count down can be stopped, released, paused, restarted.
 * There is only one Timer for the whole application.
 * 
 * A Monitor should be implemented to handle the refresh every second.
 * A Notification should be implemented to handle the end of the timer.
 * @author seb.darocha
 *
 */
public class Timer {
	static Timer mTimer = null;
	private CountDownTimer mCountDown = null;
	private long mInitialCount = 0; /** Initial count in seconds*/
	private LinkedList<Notification> mNotfications = null;
	private LinkedList<Monitor> mMonitor = null;
	
	private long mLastTick = 0; /** last tick in milliseconds (used for pause)*/
	
	/**
	 * Factory method handles a singleton object of Timer.
	 * @return the Timer object
	 */
	static public Timer getTimer()
	{
		if( mTimer == null)
			mTimer = new Timer();
		
		return mTimer;
	}
	
	/**
	 * Constructor only to be used by the factory method 
	 */
	protected Timer()
	{
		mNotfications = new LinkedList<Notification>();
		mMonitor = new LinkedList<Monitor>();
	}
	
	/**
	 * Stops the timer (without notification), and sets the count down to the parameters
	 * @param time is a "HH:MM:SS" or "MM:SS" formated string
	 * @deprecated not implemented yet
	 */
	public void setTimer( String time)
	{
		// TODO
	}
	/**
	 * Stops the timer (without notification), and sets the count down to the parameters
	 * @param hours
	 * @param minutes
	 * @param seconds
	 */
	public void setTimer( int hours, int minutes, int seconds)
	{
		setTimer( hours*60*60 + minutes * 60 + seconds);
	}

	/**
	 * Stops the timer (without notification), and sets the count down to the parameters
	 * @param hours
	 * @param minutes
	 * @param seconds
	 */
	public void setTimer( long seconds)
	{
		setTimer( seconds, true);
	}

	/**
	 * Stops the timer (without notification), and sets the count down to the parameters
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @param eraseTimer true will initialize the mInitialCount, false will not(used for pause)
	 */
	protected void setTimer( long seconds, boolean eraseTimer)
	{
		if(eraseTimer == true)
		{
			mInitialCount = seconds;
		}
		
		if(mCountDown != null)
		{
			mCountDown.cancel();
			mCountDown = null;
		}

		mCountDown = new CountDownTimer(seconds * 1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				mLastTick = millisUntilFinished/1000;
				refreshMonitors(mLastTick);
			}
			
			@Override
			public void onFinish() {
				Iterator<Notification> itNotification = mNotfications.iterator();
				while(itNotification.hasNext())
				{
					Notification not = itNotification.next();
					not.onStop();
				}
			}
		};
		refreshMonitors(seconds);
	}

	protected void refreshMonitors(long currentTime )
	{
		Iterator<Monitor> itMonitor = mMonitor.iterator();
		while(itMonitor.hasNext())
		{
			Monitor not = itMonitor.next();
			not.Refresh( mInitialCount, currentTime);
		}	
	}
	/**
	 * Starts the count down
	 */
	public void start()
	{
		mCountDown.start();
		refreshMonitors(mLastTick);
	}
	
	/**
	 * Stops the count down and reinitialize it from the beginning
	 */
	public void stop ()
	{
		mCountDown.cancel();
		mLastTick = mInitialCount;
		setTimer(mInitialCount);
	}
	
	/**
	 * Stop the count, call start() to continue.
	 * Known bug: will restart from the last tick. It has a 1 second precision.
	 */
	public void pause ()
	{
		mCountDown.cancel();
		setTimer(mLastTick, false);
	}
	
	
	/**
	 * Add a Monitor that will be called every second
	 * @param mon
	 */
	public void addMonitor(Monitor mon)
	{
		mMonitor.add(mon);
	}
	
	/**
	 * Stops the timer and release the {@link Monitor}s
	 */
	public void releaseMonitor()
	{
		// TODO look how to disable CountDownTimer link
		if(mCountDown != null)
		{
			mCountDown.cancel();
			mCountDown = null;
		}
		mMonitor.clear();
	}
	
	
	/**
	 * Add a notification that will be called at the end of the count down
	 * @param not
	 */
	public void addNotification( Notification not)
	{
		mNotfications.add(not);
	}

	/**
	 * Stops the timer and release the {@link Notification}s
	 */
	public void releaseNotification()
	{
		// TODO look how to disable CountDownTimer link
		if(mCountDown != null)
		{
			mCountDown.cancel();
			mCountDown = null;
		}
		mNotfications.clear();
	}	
}
