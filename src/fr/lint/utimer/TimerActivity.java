package fr.lint.utimer;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

import fr.lint.utimer.TimePickerDialogFragment.OnTimePickListener;
import fr.lint.utimer.util.Monitor;
import fr.lint.utimer.util.Time;
import fr.lint.utimer.util.TimerAlert;
import fr.lint.utimer.util.Timer;

import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TimerActivity extends Activity implements OnTimePickListener, TimerAlert {
	TextView timerView = null;
	ProgressBar timerProgressView = null;
	ArrayList<TimerButton> buttons = new ArrayList<TimerButton>();
	protected int lastButtonEdited = 0;
	private UserSettings settings = null;
	private ImageButton playButton = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);
		
		timerView = (TextView) findViewById(R.id.textViewCounter);
		timerProgressView = (ProgressBar) findViewById(R.id.progressBar1);
		playButton = (ImageButton) findViewById(R.id.button_pause);
		
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
		
		loadTimerButtons();
	}
	
	protected void loadTimerButtons()
	{
		LinkedList<Long> timers = settings.getTimings();
		Iterator<Long> itTimer = timers.iterator();
		
		while( itTimer.hasNext())
		{
			Time time = new Time (itTimer.next());
			addTimerButton(time.getHours(), time.getMinutes(), time.getSeconds());
		}

	}
	
	private void setPlayButton( boolean isPlay)
	{
		if( isPlay)
			playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
		else
			playButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_add_time:
	    		addTimerButton(0, 0, 0);
	    		saveTimers();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public boolean addTimerButton ( final int hours, final int minutes, final int seconds)
	{
		LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);

		TimerButton dynaButton = new TimerButton(this, hours, minutes, seconds);
		
		buttons.add(dynaButton);
		dynaButton.setPosition(buttons.lastIndexOf(dynaButton));
		buttonLayout.addView(dynaButton);
		return true;
	}
	
	public void showTimePickDialog( TimerButton button) {
		
		lastButtonEdited = button.getPosition();

		// DialogFragment.show() will take care of adding the fragment
		// in a transaction.  We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		
		// Create and show the dialog.
		DialogFragment newFragment = TimePickerDialogFragment.newInstance(button.getHours(), button.getMinutes(), button.getSeconds());
		newFragment.show(ft, "dialog");
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
		// TODO Wake up the screen
//		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//		pm.wakeUp( System.currentTimeMillis());
		
		// The screen can go off again
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setPlayButton(true);
		
		// Vibration
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(500);
		
	}

	@Override
	public void onTimerStart() {
		// prevent the screen shut off when the timer is running
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setPlayButton(false);
	}

	@Override
	public void onTimerPause() {
		// The screen can go off again
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setPlayButton(true);
	}

	@Override
	public void onTimerForcedStop() {
		// The screen can go off again
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setPlayButton(true);
	}

	@Override
	public void onTimeSet(int hour, int minute, int second) {
		buttons.get(lastButtonEdited).setTime(hour, minute, second);
		saveTimers();		
	}

	@Override
	public void onRemove() {
		// Remove the button on screen
		LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
		buttonLayout.removeView(buttons.get(lastButtonEdited));
		
		//Find the button in the list and remove it
		buttons.remove(lastButtonEdited);
		
		// Refresh the positions of all the timer buttons		
		for( int i = 0; i<buttons.size(); i++)
		{
			buttons.get(i).setPosition(i);
		}
		
		// Save the buttons
		saveTimers();
	}
}
