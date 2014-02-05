package fr.lint.utimer;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TimePickerDialogFragment extends DialogFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View result = super.onCreateView(inflater, container, savedInstanceState);
		
		View v = inflater.inflate(R.layout.timepicker, container);
		return result; 		
	}

	public TimePickerDialogFragment() {
		// TODO Auto-generated constructor stub
	}

}
