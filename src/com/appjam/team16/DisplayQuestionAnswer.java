package com.appjam.team16;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DisplayQuestionAnswer extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_question_answer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_question_answer, menu);
		return true;
	}

}
