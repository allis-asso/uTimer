package fr.lint.utimer.util;

import java.util.Locale;

public class Time
{
	protected long time = 0; // time in seconds
	
	public Time (long time)
	{
		this.time = time;
	}

	@Deprecated
	public Time (String time)
	{
		// TODO implement
		//this.time = time;
	}
	

	public Time( int hours, int minutes, int seconds)
	{
		time = toTime( hours, minutes, seconds);
	}
	
	
	public int getHours()
	{
		return (int) ((time % (60*60*60)) / (60*60));
	}
	public int getMinutes()
	{
		return (int) ((time % (60*60)) / (60));
	}
	public int getSeconds()
	{
		return (int) (time % 60);
	}


	
	public static long getHours(long time)
	{
		return (time % (60*60*60)) / (60*60);
	}
	public static long getMinutes(long time)
	{
		return (time % (60*60)) / (60);
	}
	public static long getSeconds(long time)
	{
		return (time % 60);
	}


	public static String toString(long time)
	{
		long hours = 	(time % (60*60*60)) / (60*60);
		long minutes = 	(time % (60*60)) / (60);
		long secs = 	(time % 60);
		
		return String.format( Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs); 

	}

	public static String toString(int hours, int minutes, int seconds)
	{
		return String.format( Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds); 
	}
	
	
	public static long toTime( int hours, int minutes, int seconds)
	{
		return hours*60*60 + minutes * 60 + seconds;
	}

}
