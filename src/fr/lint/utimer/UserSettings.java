package fr.lint.utimer;

import java.util.Iterator;
import java.util.LinkedList;

import android.content.SharedPreferences;

public class UserSettings {
	final private String NUMBER_OF_BUTTONS = "NB_BUTTONS";
	final private String BUTTON_ = "BUTTONS_";
	
	protected SharedPreferences settings = null;
	private LinkedList<Long> timers = new LinkedList<Long>();

	public UserSettings(SharedPreferences settings) {
		this.settings = settings;
		
		long nb_buttons = settings.getLong(NUMBER_OF_BUTTONS, 4);
		
		for(int i = 0; i<nb_buttons; i++)
		{
			timers.add(settings.getLong(BUTTON_ + i, 0));
		}
	}

	public LinkedList<Long> getTimings()
	{
		return timers;
	}

	public void clearTimings()
	{
		timers = new LinkedList<Long>();
	}
	
	public void setTimings(LinkedList<Long> timers)
	{
		this.timers = timers;
		saveTimings();
	}
	
	private void saveTimings()
	{
		SharedPreferences.Editor editor = settings.edit();

		editor.putLong(NUMBER_OF_BUTTONS, timers.size());

		Iterator<Long> it = timers.iterator();
		int i = 0;
		while( it.hasNext())
		{
			editor.putLong(BUTTON_ + i++, it.next());
		}

		// Commit the edits!
		editor.commit();
	}
}
