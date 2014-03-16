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
		return (int) ((time % (100*60*60)) / (60*60));
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
		return (time % (100*60*60)) / (60*60);
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
		long hours = 	(time % (100*60*60)) / (60*60);
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
		return hours*60*60 + minutes*60 + seconds;
	}

}
