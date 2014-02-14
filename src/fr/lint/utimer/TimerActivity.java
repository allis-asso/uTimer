package fr.lint.utimer;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

import fr.lint.utimer.util.Monitor;
import fr.lint.utimer.util.TimerAlert;
import fr.lint.utimer.util.Timer;

import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimerActivity extends Activity implements TimePickerDialog.OnTimeSetListener, TimerAlert {
	TextView timerView = null;
	ProgressBar timerProgressView = null;
	ArrayList<TimerButton> buttons = new ArrayList<TimerButton>();
	protected int lastButtonEdited = 0;
	private UserSettings settings = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);
		
		timerView = (TextView) findViewById(R.id.textViewCounter);
		timerProgressView = (ProgressBar) findViewById(R.id.progressBar1);

		// Enable the timer
		Timer timer = Timer.getTimer();
		
		// Enable the refresh
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
		
		// Enable the Alert
		timer.addNotification(this);
		
		settings = new UserSettings(getPreferences(Context.MODE_PRIVATE));
		
		addTimerButton(0, 0, 30);
		addTimerButton(0, 1, 0);
		addTimerButton(0, 1, 30);
		addTimerButton(0, 2, 0);
	}
	
	public void onStartPause(View v)
	{
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

	public void onStop(View v)
	{
		// The screen can go off again
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		Timer timer = Timer.getTimer();
		timer.stop();
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

		TimerButton dynaButton = new TimerButton(this, hours, minutes, seconds);
		
		buttons.add(dynaButton);
		dynaButton.setPosition(buttons.lastIndexOf(dynaButton));
		buttonLayout.addView(dynaButton);
		return true;
	}
	
	public void showTimePickDialog( TimerButton button) {
		
		// DialogFragment.show() will take care of adding the fragment
		// in a transaction.  We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		
		// Prepare args
		Bundle b = new Bundle();
		b.putInt(TimePickerDialogFragment.HOUR, button.getHours());
		b.putInt(TimePickerDialogFragment.MINUTE, button.getMinutes());
		b.putInt(TimePickerDialogFragment.SECOND, button.getSeconds());
		lastButtonEdited = button.getPosition();

		// Create and show the dialog.
		DialogFragment newFragment = new TimePickerDialogFragment();
		newFragment.setArguments(b);
		newFragment.show(ft, "dialog");
	}
	
	@Override
	public void onTimeSet(TimePicker arg0, int minutes, int seconds) {
		buttons.get(lastButtonEdited).setTime(0, minutes, seconds);
		saveTimers();
	}
	
	private void saveTimers()
	{
		Iterator<TimerButton> it = buttons.iterator();
		LinkedList<Long> times = new LinkedList<Long>();
		
		while( it.hasNext())
		{
			times.add( it.next().getTime());
		}
		settings.setTimings(times);
	}
	

	@Override
	public void onTimerStop() {
		// Wake up the screen
//		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//		pm.wakeUp( System.currentTimeMillis());
		
		// The screen can go off again
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// Vibration
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(500);
		
	}

	@Override
	public void onTimerStart() {
		// prevent the screen shut off when the timer is running
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public void onTimerPause() {
		// The screen can go off again
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
	}

	@Override
	public void onTimerForcedStop() {
		// The screen can go off again
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
	}
	
}
