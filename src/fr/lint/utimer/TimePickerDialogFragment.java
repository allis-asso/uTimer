package fr.lint.utimer;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
	public static final String HOUR = "HOUR";
	public static final String MINUTE = "MINUTE";
	public static final String SECOND = "SECS";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO set the maximum time lengths in the specs
		int hour = getArguments().getInt(HOUR);
		int minute = getArguments().getInt(MINUTE);
		int secs = getArguments().getInt(SECOND);
		
		// Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, minute, secs, true);
	}

	@Override
	public void onTimeSet(TimePicker arg0, int minutes, int seconds) {
		// TODO Auto-generated method stub
		
	}

}
