package fr.lint.utimer;

import java.util.Locale;

import fr.lint.utimer.util.Timer;

import android.view.View;
import android.widget.Button;

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
				Timer timer = Timer.getTimer();
				timer.setTimer( TimerButton.this.hours, TimerButton.this.minutes, TimerButton.this.seconds);
				timer.start();
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

	

}
