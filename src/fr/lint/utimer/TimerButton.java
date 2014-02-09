package fr.lint.utimer;

import java.util.Locale;

import fr.lint.utimer.util.Timer;

import android.view.View;
import android.widget.Button;

public class TimerButton extends Button {
	protected int hours = 0;
	protected int minutes = 0;
	protected int seconds = 0;
	
	public TimerButton( final TimerActivity context, final int hours, final int minutes, final int seconds) {
		super(context);
		
		String display = String.format( Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
		setText(display);

		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Timer timer = Timer.getTimer();
				timer.setTimer(hours, minutes, seconds);
				// TODO Implement notification				
				timer.start();
			}
		});
		
		setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				context.showTimePickDialog(hours, minutes,seconds);
				return true;
			}
		});

	}

}
