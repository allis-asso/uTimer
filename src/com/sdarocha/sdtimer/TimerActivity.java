package com.sdarocha.sdtimer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class TimerActivity extends Activity {
	TextView timer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);
		
		timer = (TextView) findViewById(R.id.textViewCounter);
		
		final View button1 = findViewById(R.id.button1);
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new CountDownTimer((1*60+0)*1000, 1000){

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						
					}

					@SuppressLint("DefaultLocale")
					@Override
					public void onTick(long millisUntilFinished) {
						// TODO Auto-generated method stub
						long hours = 	(millisUntilFinished % (60*60*60*1000)) / (60*60*1000);
						long minutes = 	(millisUntilFinished % (60*60*1000)) / (60*1000);
						long secs = 	(millisUntilFinished % (60*1000)) / (1000);
						String display = String.format("%02d:%02d:%02d", hours, minutes, secs); 
						timer.setText(display);
					}
				
				}.start();

			}
		
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timer, menu);
		return true;
	}

}
