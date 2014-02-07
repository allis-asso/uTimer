package fr.lint.utimer;


import java.util.Locale;

import fr.lint.utimer.util.Monitor;
import fr.lint.utimer.util.Timer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TimerActivity extends Activity {
	TextView timerView = null;
	ProgressBar timerProgressView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);
		
		timerView = (TextView) findViewById(R.id.textViewCounter);
		timerProgressView = (ProgressBar) findViewById(R.id.progressBar1);
		final View buttonPause = findViewById(R.id.button_pause);
		final View buttonStop = findViewById(R.id.button_stop);

		// Enable the timer

		Timer timer = Timer.getTimer();
		Monitor refreshTimeView = new Monitor() {
			
			@Override
			public void Refresh(long initialTime, long currentTime) {
				long hours = 	(currentTime % (60*60*60)) / (60*60);
				long minutes = 	(currentTime % (60*60)) / (60);
				long secs = 	(currentTime % 60);
				String display = String.format( Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs); 
				timerView.setText(display);
				
				timerProgressView.setMax( (int) initialTime);
				timerProgressView.setProgress( (int)(initialTime-currentTime));
			}
		};
		timer.addMonitor(refreshTimeView);
		
		addTimerButton(0, 0, 30);
		addTimerButton(0, 1, 0);
		addTimerButton(0, 1, 30);
		addTimerButton(0, 2, 0);

		//TODO Ajouter une image play
		buttonPause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Timer timer = Timer.getTimer();
				switch(timer.getStatus())
				{
				case eTimerStatusStop:
				case eTimerStatusPause:
					timer.start();
					break;
				case eTimerStatusPlay:
					timer.pause();
					break;
				default:
					break;
				}
			}
		});
		buttonStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Timer timer = Timer.getTimer();
				timer.stop();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timer, menu);
		return true;
	}

	public boolean addTimerButton ( final int hours, final int minutes, final int seconds)
	{
		final LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);

		Button dynaButton = new Button(this);
		
		String display = String.format( Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
		dynaButton.setText(display);

		dynaButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Timer timer = Timer.getTimer();
				timer.setTimer(hours, minutes, seconds);
				// TODO Implement notification				
				timer.start();
			}
		});
		
		dynaButton.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				
				return true;
			}
		});

		buttonLayout.addView(dynaButton);
		return true;
	}
}
