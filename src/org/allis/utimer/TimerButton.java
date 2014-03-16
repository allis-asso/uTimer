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

package org.allis.utimer;

import java.util.Locale;

import org.allis.util.Timer;


import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;

@SuppressLint("ViewConstructor")
public class TimerButton extends Button {
	protected int hours = 0;
	protected int minutes = 0;
	protected int seconds = 0;
	protected int position = 0;

	public TimerButton( final TimerActivity context, int hours, int minutes, int seconds) {
		super(context);

		setTime(hours, minutes, seconds);

		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// If the button is not set up (time null)
				if( TimerButton.this.hours+TimerButton.this.minutes+TimerButton.this.seconds == 0 )
				{
					context.showTimePickDialog(TimerButton.this);
				}
				else
				{
					Timer timer = Timer.getTimer();
					timer.setTimer( TimerButton.this.hours, TimerButton.this.minutes, TimerButton.this.seconds);
					timer.start();
				}
			}
		});

		setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				context.showTimePickDialog(TimerButton.this);
				return true;
			}
		});
	}
		
		
	public void setTime(int hours, int minutes, int seconds) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		
		String display = String.format( Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
		setText(display);
	}

	public int getHours() {
		return hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getSeconds() {
		return seconds;
	}


	public int getPosition() {
		return position;
	}


	public void setPosition(int position) {
		this.position = position;
	}


	public long getTime() {
		return ((hours*60+minutes)*60 + seconds) ;
	}

}
