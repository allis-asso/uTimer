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

import org.allis.utimer.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TimePickerDialogFragment extends DialogFragment implements OnClickListener {
	
	public interface OnTimePickListener {

		public void onTimeSet( int hour, int minute, int second);
		public void onRemove();

	}

	public static final String HOUR = "HOUR";
	public static final String MINUTE = "MINUTE";
	public static final String SECOND = "SECS";

	OnTimePickListener listener = null;
	
	protected TextView evHour = null;
	protected TextView evMin = null;
	protected TextView evSec = null;
	
	protected Button butAddHour = null;
	protected Button butAddMin = null;
	protected Button butAddSec = null;

	protected Button butRemHour = null;
	protected Button butRemMin = null;
	protected Button butRemSec = null;

	static TimePickerDialogFragment newInstance( int hours, int minutes, int seconds)
	{
		TimePickerDialogFragment timePicker = new TimePickerDialogFragment();
		Bundle b = new Bundle();
		b.putInt(TimePickerDialogFragment.HOUR, hours);
		b.putInt(TimePickerDialogFragment.MINUTE, minutes);
		b.putInt(TimePickerDialogFragment.SECOND, seconds);

		timePicker.setArguments(b);

		return timePicker;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.timepicker, container, false);

        getDialog().setTitle(R.string.setup_time);
		int hour = getArguments().getInt(HOUR);
		int minute = getArguments().getInt(MINUTE);
		int secs = getArguments().getInt(SECOND);
		
		listener = (TimerActivity) getActivity();

		View butOK = v.findViewById(R.id.BtnColorPickerOk);
		View butRem = v.findViewById(R.id.button_time_picker_remove);
		View butCancel = v.findViewById(R.id.BtnColorPickerCancel);
		
		// TODO verify the time before accepting (or limit the edition to a simple view)
		
		// set the time into the TextViews
		evHour = (TextView) v.findViewById(R.id.editHours);
		evHour.setText(String.format( Locale.getDefault(), "%02d", hour));
		
		evMin = (TextView) v.findViewById(R.id.editMinutes);
		evMin.setText(String.format( Locale.getDefault(), "%02d", minute));
		
		evSec = (TextView) v.findViewById(R.id.editSeconds);
		evSec.setText(String.format( Locale.getDefault(), "%02d", secs));

		// add + and - buttons behavior
		butAddHour = (Button) v.findViewById(R.id.button_hour_add);
		butAddHour.setOnClickListener(this);
		butAddMin = (Button) v.findViewById(R.id.button_minute_add);
		butAddMin.setOnClickListener(this);
		butAddSec = (Button) v.findViewById(R.id.button_second_add);
		butAddSec.setOnClickListener(this);
		butRemHour = (Button) v.findViewById(R.id.button_hour_rem);
		butRemHour.setOnClickListener(this);
		butRemMin = (Button) v.findViewById(R.id.button_minute_rem);
		butRemMin.setOnClickListener(this);
		butRemSec = (Button) v.findViewById(R.id.button_second_rem);
		butRemSec.setOnClickListener(this);
		
		butOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onValidation();
				
			}
		});
		
		butRem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onRemove();
				
			}
		});

		butCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onCancel();
				
			}
		});
		return v;
    }

	public void onValidation()
	{
		// get the time from the buttons
		int hour = validate(evHour.getText().toString(), 99, 0);
		int minute = validate(evMin.getText().toString(), 59, 0);
		int second = validate(evSec.getText().toString(), 59, 0);
		
		
		dismissAllowingStateLoss();

		// send the time to the OnTimePickListener
		listener.onTimeSet(hour, minute, second);
	}
	
	private int validate (String value, int max, int min)
	{
		int iValue = 0;
		try{
			iValue = Integer.parseInt(value);
			// If hour is bigger than max hours, set it to max
			if( iValue >= max)
			{
				iValue  =max;
			}
			// Lesser than min , set to min
			if( iValue <= min)
			{
				iValue = min;
			}
		} catch( NumberFormatException nfe)
		{
			nfe.printStackTrace();
		}
		return iValue;
	}
	
	public void onCancel()
	{
		dismissAllowingStateLoss();
	}
	
	/**
	 *  Dismiss and call the OnTimePickListener to destroy the timing
	 */
	public void onRemove()
	{
		dismissAllowingStateLoss();

		listener.onRemove();
	}

	@Override
	public void onClick(View v) {
		
		if( v.equals( butAddHour))
		{
			correctTextView( evHour, 100, true);
		}
		else if( v.equals( butRemHour))
		{
			correctTextView( evHour, 100, false);
		}
		else if( v.equals( butAddMin))
		{
			correctTextView( evMin, 60, true);
		}
		else if( v.equals( butRemMin))
		{
			correctTextView( evMin, 60, false);
		}
		else if( v.equals( butAddSec))
		{
			correctTextView( evSec, 60, true);
		}
		else if( v.equals( butRemSec))
		{
			correctTextView( evSec, 60, false);
		}
		
	}
	
	private static void correctTextView( TextView tv, int maximum, boolean increase)
	{
		int time = Integer.parseInt(tv.getText().toString());
		if( increase == true)
		{
			time = (time+1)%maximum;
		}
		else
		{
			if (time == 0)
				time = maximum - 1;
			else
				time = (time-1)%maximum;
		}
		tv.setText(String.format( Locale.getDefault(), "%02d", time));
	}

}
