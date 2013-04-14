package com.ashwin.helloandroid;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity implements OnSeekBarChangeListener {

	private SeekBar seekBar;
	private FrameLayout frameLayout;
	private int prevValue = (int) (50*2.55);
	private TextView textView;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        frameLayout = (FrameLayout) findViewById(R.id.mainLayout);
        textView = (TextView) findViewById(R.id.textView);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(50);
        
    }

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		int newValue = (int) (arg1 * 2.55);
		int pColor = Color.rgb(prevValue, prevValue, prevValue);
		int nColor = Color.rgb(newValue, newValue, newValue);
		ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), 
				pColor, nColor);
		colorAnimation.addUpdateListener(new AnimatorUpdateListener() {

		    @Override
		    public void onAnimationUpdate(ValueAnimator animator) {
		        frameLayout.setBackgroundColor((Integer)animator.getAnimatedValue());
		    }

		});
		colorAnimation.start();
		
		String text = "I'm so happ";
		for (int i = 0; i < (arg1/5); i++)
			text += "p";
		text += "y";
		textView.setText(text);
		
		prevValue = newValue;
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
}
