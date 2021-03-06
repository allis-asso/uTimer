/*
µTimer, a simple timer program
Copyright (C) 2014  ALLIS

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.allis.util;

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
	
	public enum TimerStatus { eTimerStatusStop,eTimerStatusPlay, eTimerStatusPause };
	private TimerStatus status = TimerStatus.eTimerStatusStop;

	private CountDownTimer mCountDown = null;
	
	private long mInitialCount = 0; /** Initial count in seconds*/
	private long mLastTick = 0; /** last tick in seconds (used for pause)*/
	
	private LinkedList<TimerAlert> mNotfications = null;
	private LinkedList<Monitor> mMonitor = null;
		
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
	
	public TimerStatus getStatus() {
		return status;
	}

	/**
	 * Constructor only to be used by the factory method 
	 */
	protected Timer()
	{
		mNotfications = new LinkedList<TimerAlert>();
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
	public void setTimer( int seconds)
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

		mLastTick = seconds;
		mCountDown = new CountDownTimer(seconds * 1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				mLastTick = millisUntilFinished/1000;
				refresh(mLastTick);
			}
			
			@Override
			public void onFinish() {
				stop();
				Iterator<TimerAlert> itNotification = mNotfications.iterator();
				while(itNotification.hasNext())
				{
					TimerAlert not = itNotification.next();
					not.onTimerStop();
				}
			}
		};
		refresh(mLastTick);
	}

	private void refresh( long secsUntilFinished)
	{
		Iterator<Monitor> itMonitor = mMonitor.iterator();
		while(itMonitor.hasNext())
		{
			Monitor not = itMonitor.next();
			not.Refresh( mInitialCount, secsUntilFinished);
		}
	}
		
	/**
	 * Starts the count down
	 */
	public void start()
	{
		status = TimerStatus.eTimerStatusPlay;
		if(mCountDown != null)
		{
			mCountDown.start();
			Iterator<TimerAlert> itNotification = mNotfications.iterator();
			while(itNotification.hasNext())
			{
				TimerAlert not = itNotification.next();
				not.onTimerStart();
			}
		}
		refresh(mInitialCount);
	}
	
	/**
	 * Stops the count down and reinitialize it from the beginning
	 */
	public void stop ()
	{
		status = TimerStatus.eTimerStatusStop;
		if(mCountDown != null)
		{
			mCountDown.cancel();
			Iterator<TimerAlert> itNotification = mNotfications.iterator();
			while(itNotification.hasNext())
			{
				TimerAlert not = itNotification.next();
				not.onTimerForcedStop();
			}

		}
		setTimer(mInitialCount, false);
	}
	
	/**
	 * Stop the count, call start() to continue.
	 * Known bug: will restart from the last tick. It has a 1 second precision.
	 */
	public void pause ()
	{
		status = TimerStatus.eTimerStatusPause;
		if(mCountDown != null)
		{
			mCountDown.cancel();
			Iterator<TimerAlert> itNotification = mNotfications.iterator();
			while(itNotification.hasNext())
			{
				TimerAlert not = itNotification.next();
				not.onTimerPause();
			}
		}
		setTimer(mLastTick, false);
	}
	
	
	/**
	 * Add a Monitor that will be called every second
	 * @param mon
	 */
	public void addMonitor(Monitor mon)
	{
		mMonitor.add(mon);
		refresh(mLastTick);
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
	public void addNotification( TimerAlert not)
	{
		mNotfications.add(not);
	}

	/**
	 * Stops the timer and release the {@link TimerAlert}s
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
