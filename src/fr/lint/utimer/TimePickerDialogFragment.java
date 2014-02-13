package fr.lint.utimer;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;

public class TimePickerDialogFragment extends DialogFragment  {
	public static final String HOUR = "HOUR";
	public static final String MINUTE = "MINUTE";
	public static final String SECOND = "SECS";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO set the maximum time lengths in the specs
		//int hour = getArguments().getInt(HOUR);
		int minute = getArguments().getInt(MINUTE);
		int secs = getArguments().getInt(SECOND);
		
		// Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), (TimerActivity) getActivity(), minute, secs, true);
	}


}
